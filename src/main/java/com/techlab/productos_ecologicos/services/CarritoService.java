
package com.techlab.productos_ecologicos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techlab.productos_ecologicos.dto.CarritoResponseDTO;
import com.techlab.productos_ecologicos.dto.CarritoResumenDTO;
import com.techlab.productos_ecologicos.exception.CarritoNoEncontradoException;
import com.techlab.productos_ecologicos.exception.ProductoNoEncontradoEnElCarritoException;
import com.techlab.productos_ecologicos.exception.StockInsuficienteException;
import com.techlab.productos_ecologicos.exception.CarritoConPedidoPendienteException;
import com.techlab.productos_ecologicos.mapper.CarritoMapper;
import com.techlab.productos_ecologicos.models.Carrito;
import com.techlab.productos_ecologicos.models.CarritoProducto;
import com.techlab.productos_ecologicos.models.EstadoCarrito;
import com.techlab.productos_ecologicos.models.EstadoPedido;
import com.techlab.productos_ecologicos.models.Producto;
import com.techlab.productos_ecologicos.models.Usuario;
import com.techlab.productos_ecologicos.repository.CarritoProductoRepository;
import com.techlab.productos_ecologicos.repository.CarritoRepository;
import com.techlab.productos_ecologicos.repository.PedidoRepository;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoService productoService;
    private final CarritoProductoRepository carritoProductoRepository;
    private final PedidoRepository pedidoRepository;

    /**
     * Inicializa las dependencias necesarias para gestionar
     * carritos, productos y pedidos.
     *
     * @param carritoRepository repositorio de carritos.
     * @param carritoProductoRepository repositorio de productos del carrito.
     * @param productoService servicio de gestión de productos.
     * @param pedidoRepository repositorio de pedidos.
     */
    public CarritoService(
            CarritoRepository carritoRepository,
            CarritoProductoRepository carritoProductoRepository,
            ProductoService productoService,
            PedidoRepository pedidoRepository) {

        this.carritoRepository = carritoRepository;
        this.productoService = productoService;
        this.carritoProductoRepository = carritoProductoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Crea un carrito asociado al usuario autenticado.
     *
     * @return carrito creado y guardado.
     */
    @Transactional
    public Carrito crear() {
        Usuario usuario = (Usuario) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setEstado(EstadoCarrito.ACTIVO);

        return carritoRepository.save(carrito);
    }

    /**
     * Busca un carrito por su identificador.
     *
     * @param id identificador del carrito.
     * @return carrito encontrado.
     * @throws CarritoNoEncontradoException si el carrito no existe.
     */
    @Transactional(readOnly = true)
    public Carrito obtenerPorId(Integer id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNoEncontradoException(
                        "No se encontró un carrito con id " + id));
    }

    /**
     * Obtiene todos los carritos registrados.
     *
     * @return lista de carritos.
     */
    @Transactional(readOnly = true)
    public List<Carrito> listarTodos() {
        return carritoRepository.findAll();
    }

    /**
     * Agrega una unidad de un producto al carrito indicado.
     * Comprueba la disponibilidad actual sin reservar ni descontar stock.
     *
     * @param carritoId identificador del carrito.
     * @param productoId identificador del producto.
     * @return carrito actualizado.
     * @throws StockInsuficienteException si no hay unidades disponibles
     *         para la cantidad solicitada.
     */
    @Transactional
    public Carrito agregarProducto(Integer carritoId, Integer productoId) {
        Carrito carrito = obtenerPorId(carritoId);
        validarCarritoSinPedidoPendiente(carrito);

        Producto producto = productoService.obtenerPorId(productoId);

        Optional<CarritoProducto> existente =
                carritoProductoRepository.findByCarritoAndProducto(
                        carrito, producto);

        int cantidadActual = existente
                .map(CarritoProducto::getCantidad)
                .orElse(0);

        int stockDisponible = producto.getStock() == null
                ? 0
                : producto.getStock();

        if (stockDisponible <= 0
                || cantidadActual >= stockDisponible) {
            throw new StockInsuficienteException(
                    "No hay stock suficiente para agregar otra unidad de \""
                            + producto.getNombre() + "\".");
        }

        if (existente.isPresent()) {
            CarritoProducto cp = existente.get();
            cp.setCantidad(cantidadActual + 1);
            carritoProductoRepository.save(cp);
        } else {
            CarritoProducto nuevo =
                    new CarritoProducto(null, carrito, producto, 1);
            carritoProductoRepository.save(nuevo);
        }

        // El carrito guarda la intención de compra, sin modificar el inventario.
        return carritoRepository.save(carrito);
    }

    /**
     * Agrega una unidad al carrito activo del usuario autenticado.
     *
     * @param productoId identificador del producto.
     * @return carrito actualizado.
     */
    @Transactional
    public Carrito agregarProducto(Integer productoId) {
        Carrito carrito = obtenerCarritoDelUsuario();
        return agregarProducto(carrito.getId(), productoId);
    }

    /**
     * Descuenta una unidad de un producto del carrito.
     * No modifica el stock del inventario.
     *
     * @param carritoId identificador del carrito.
     * @param productoId identificador del producto.
     * @return carrito actualizado.
     */
    @Transactional
    public Carrito descontarProducto(Integer carritoId, Integer productoId) {
        Carrito carrito = obtenerPorId(carritoId);
        validarCarritoSinPedidoPendiente(carrito);

        Producto producto = productoService.obtenerPorId(productoId);

        CarritoProducto carritoProducto =
                carritoProductoRepository
                        .findByCarritoAndProducto(carrito, producto)
                        .orElseThrow(() ->
                                new ProductoNoEncontradoEnElCarritoException(
                                        "El producto no se encuentra en el carrito."));

        if (carritoProducto.getCantidad() > 1) {
            carritoProducto.setCantidad(
                    carritoProducto.getCantidad() - 1);
            carritoProductoRepository.save(carritoProducto);
        } else {
            carritoProductoRepository.delete(carritoProducto);
        }

        return carritoRepository.save(carrito);
    }

    /**
     * Descuenta una unidad del producto en el carrito activo
     * del usuario autenticado.
     *
     * @param productoId identificador del producto.
     * @return carrito actualizado.
     */
    @Transactional
    public Carrito descontarProducto(Integer productoId) {
        Carrito carrito = obtenerCarritoDelUsuario();
        return descontarProducto(carrito.getId(), productoId);
    }

    /**
     * Elimina todas las unidades de un producto del carrito.
     * No devuelve unidades al stock porque el carrito no las reserva.
     *
     * @param carritoId identificador del carrito.
     * @param productoId identificador del producto.
     * @return carrito actualizado.
     */
    @Transactional
    public Carrito eliminarProducto(Integer carritoId, Integer productoId) {
        Carrito carrito = obtenerPorId(carritoId);
        validarCarritoSinPedidoPendiente(carrito);

        Producto producto = productoService.obtenerPorId(productoId);

        CarritoProducto cp = carritoProductoRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElseThrow(() ->
                        new ProductoNoEncontradoEnElCarritoException(
                                "El producto no se encuentra en el carrito."));

        // Elimina solamente la relación del producto con el carrito.
        carritoProductoRepository.delete(cp);

        return carritoRepository.save(carrito);
    }

    /**
     * Elimina un producto del carrito activo del usuario autenticado.
     *
     * @param productoId identificador del producto.
     * @return carrito actualizado.
     */
    @Transactional
    public Carrito eliminarProducto(Integer productoId) {
        Carrito carrito = obtenerCarritoDelUsuario();
        return eliminarProducto(carrito.getId(), productoId);
    }

    /**
     * Vacía el carrito sin modificar las cantidades del inventario.
     *
     * @param carritoId identificador del carrito.
     * @return carrito vacío.
     */
    @Transactional
    public Carrito vaciar(Integer carritoId) {
        Carrito carrito = obtenerPorId(carritoId);
        validarCarritoSinPedidoPendiente(carrito);

        // orphanRemoval elimina los detalles retirados de la colección.
        carrito.getProductos().clear();

        return carritoRepository.save(carrito);
    }

    /**
     * Vacía el carrito activo del usuario autenticado.
     *
     * @return carrito vacío.
     */
    @Transactional
    public Carrito vaciar() {
        Carrito carrito = obtenerCarritoDelUsuario();
        return vaciar(carrito.getId());
    }

    /**
     * Elimina un carrito sin modificar el stock del inventario.
     * Impide la operación si existe un pedido pendiente asociado.
     *
     * @param id identificador del carrito.
     */
    @Transactional
    public void eliminar(Integer id) {
        Carrito carrito = obtenerPorId(id);
        validarCarritoSinPedidoPendiente(carrito);

        // Elimina el carrito y sus relaciones según el mapeo JPA.
        carritoRepository.delete(carrito);
    }

    /**
     * Calcula la cantidad de unidades, el subtotal, el envío y el total.
     *
     * @param carritoId identificador del carrito.
     * @return resumen del carrito.
     */
    @Transactional(readOnly = true)
    public CarritoResumenDTO obtenerResumen(Integer carritoId) {
        Carrito carrito = obtenerPorId(carritoId);

        int cantidad = 0;
        double subtotal = 0.0;

        for (CarritoProducto cp : carrito.getProductos()) {
            cantidad += cp.getCantidad();
            subtotal += cp.getCantidad()
                    * cp.getProducto().getPrecio();
        }

        // El envío es gratuito en la implementación actual.
        double envio = 0.0;
        double total = subtotal + envio;

        return new CarritoResumenDTO(
                cantidad,
                subtotal,
                envio,
                total);
    }

    /**
     * Calcula el resumen del carrito activo del usuario autenticado.
     *
     * @return resumen del carrito.
     */
    @Transactional(readOnly = true)
    public CarritoResumenDTO obtenerResumen() {
        Carrito carrito = obtenerCarritoDelUsuario();
        return obtenerResumen(carrito.getId());
    }

    /**
     * Obtiene el carrito activo del usuario autenticado.
     * Si no existe, crea uno nuevo.
     *
     * @return carrito activo.
     */
    @Transactional
    public Carrito obtenerCarritoDelUsuario() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) auth.getPrincipal();

        return carritoRepository
                .findByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseGet(() -> {
                    Carrito carrito = new Carrito();
                    carrito.setUsuario(usuario);
                    carrito.setEstado(EstadoCarrito.ACTIVO);
                    return carritoRepository.save(carrito);
                });
    }

    /**
     * Obtiene el carrito activo del usuario y lo convierte a DTO.
     *
     * @return carrito en formato DTO.
     */
    @Transactional(readOnly = true)
    public CarritoResponseDTO obtenerMiCarritoDTO() {
        Carrito carrito = obtenerCarritoDelUsuario();
        return CarritoMapper.toDTO(carrito);
    }

    /**
     * Obtiene un carrito por su ID y lo convierte a DTO.
     *
     * @param id identificador del carrito.
     * @return carrito en formato DTO.
     */
    @Transactional(readOnly = true)
    public CarritoResponseDTO obtenerCarritoDTO(Integer id) {
        Carrito carrito = obtenerPorId(id);
        return CarritoMapper.toDTO(carrito);
    }

    /**
     * Agrega un producto al carrito activo y devuelve el resultado como DTO.
     *
     * @param productoId identificador del producto.
     * @return carrito actualizado en formato DTO.
     */
    @Transactional
    public CarritoResponseDTO agregarProductoDTO(Integer productoId) {
        Carrito carrito = agregarProducto(productoId);
        return CarritoMapper.toDTO(carrito);
    }

    /**
     * Descuenta una unidad del carrito activo y devuelve el resultado como DTO.
     *
     * @param productoId identificador del producto.
     * @return carrito actualizado en formato DTO.
     */
    @Transactional
    public CarritoResponseDTO descontarProductoDTO(Integer productoId) {
        Carrito carrito = descontarProducto(productoId);
        return CarritoMapper.toDTO(carrito);
    }

    /**
     * Elimina un producto del carrito activo y devuelve el resultado como DTO.
     *
     * @param productoId identificador del producto.
     * @return carrito actualizado en formato DTO.
     */
    @Transactional
    public CarritoResponseDTO eliminarProductoDTO(Integer productoId) {
        Carrito carrito = eliminarProducto(productoId);
        return CarritoMapper.toDTO(carrito);
    }

    /**
     * Vacía el carrito activo y devuelve el resultado como DTO.
     *
     * @return carrito vacío en formato DTO.
     */
    @Transactional
    public CarritoResponseDTO vaciarDTO() {
        Carrito carrito = vaciar();
        return CarritoMapper.toDTO(carrito);
    }

    /**
     * Obtiene todos los carritos y los convierte a DTO.
     *
     * @return lista de carritos en formato DTO.
     */
    @Transactional(readOnly = true)
    public List<CarritoResponseDTO> obtenerCarritosResponse() {
        return carritoRepository.findAll()
                .stream()
                .map(CarritoMapper::toDTO)
                .toList();
    }

    /**
     * Obtiene todos los carritos y los convierte a DTO.
     *
     * @return lista de carritos en formato DTO.
     */
    @Transactional(readOnly = true)
    public List<CarritoResponseDTO> listarTodosDTO() {
        return listarTodos()
                .stream()
                .map(CarritoMapper::toDTO)
                .toList();
    }

    /**
     * Impide modificar o eliminar un carrito que tenga
     * un pedido pendiente de pago.
     *
     * @param carrito carrito que se desea validar.
     * @throws CarritoConPedidoPendienteException si existe un pedido pendiente.
     */
    private void validarCarritoSinPedidoPendiente(Carrito carrito) {
        boolean tienePedidoPendiente =
                pedidoRepository.existsByCarritoAndEstado(
                        carrito,
                        EstadoPedido.PENDIENTE);

        if (tienePedidoPendiente) {
            throw new CarritoConPedidoPendienteException(
                    "No se puede modificar ni eliminar el carrito porque tiene un pedido pendiente.");
        }
    }
}