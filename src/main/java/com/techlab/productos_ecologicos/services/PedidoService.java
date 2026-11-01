
package com.techlab.productos_ecologicos.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techlab.productos_ecologicos.dto.PedidoRequestDTO;
import com.techlab.productos_ecologicos.dto.PedidoResponseDTO;
import com.techlab.productos_ecologicos.exception.CarritoNoEncontradoException;
import com.techlab.productos_ecologicos.exception.PedidoCarritoVacioException;
import com.techlab.productos_ecologicos.exception.StockInsuficienteException;
import com.techlab.productos_ecologicos.mapper.PedidoMapper;
import com.techlab.productos_ecologicos.models.Carrito;
import com.techlab.productos_ecologicos.models.CarritoProducto;
import com.techlab.productos_ecologicos.models.EstadoCarrito;
import com.techlab.productos_ecologicos.models.EstadoPedido;
import com.techlab.productos_ecologicos.models.Pedido;
import com.techlab.productos_ecologicos.models.PedidoProducto;
import com.techlab.productos_ecologicos.models.Producto;
import com.techlab.productos_ecologicos.models.Usuario;
import com.techlab.productos_ecologicos.models.Pedido;
import com.techlab.productos_ecologicos.models.PedidoProducto;
import com.techlab.productos_ecologicos.models.EstadoPedido;
import com.techlab.productos_ecologicos.repository.CarritoRepository;
import com.techlab.productos_ecologicos.repository.PedidoRepository;
import com.techlab.productos_ecologicos.repository.ProductoRepository;

@Service
public class PedidoService {

    private final CarritoRepository carritoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    // Inicializa las dependencias necesarias para gestionar pedidos y reservar stock.
    public PedidoService(
            CarritoRepository carritoRepository,
            PedidoRepository pedidoRepository,
            ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    // Crea un pedido pendiente y reserva el stock de todos sus productos en una transacción.
    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO request) {
        Usuario usuario = obtenerUsuarioAutenticado();

        Carrito carrito = carritoRepository
                .findByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new CarritoNoEncontradoException(
                        "No se encontró un carrito activo para el usuario."));

        if (carrito.getProductos() == null || carrito.getProductos().isEmpty()) {
            throw new PedidoCarritoVacioException(
                    "No se puede crear un pedido con el carrito vacío.");
        }

        // Evita crear más de un pedido para el mismo carrito.
        if (pedidoRepository.existsByCarrito(carrito)) {
            throw new IllegalStateException(
                    "Ya existe un pedido asociado a este carrito.");
        }

        double subtotal = 0.0;
        List<PedidoProducto> detalles = new ArrayList<>();

        // Valida los productos y reserva el stock disponible antes de guardar el pedido.
        for (CarritoProducto item : carrito.getProductos()) {
            if (item.getProducto() == null
                    || item.getProducto().getId() == null
                    || item.getCantidad() == null
                    || item.getCantidad() <= 0) {
                throw new IllegalStateException(
                        "El carrito contiene un producto o una cantidad inválida.");
            }

            Producto producto = item.getProducto();
            Integer productoId = producto.getId();
            Integer cantidad = item.getCantidad();

            // Descuenta el stock solo si hay suficientes unidades disponibles.
            int filasActualizadas = productoRepository.reservarStockSiDisponible(
                    productoId, cantidad);

            // Si falla una reserva, la transacción revierte también las reservas anteriores.
            if (filasActualizadas != 1) {
                throw new StockInsuficienteException(
                        "No hay stock suficiente para el producto: "
                                + producto.getNombre()
                                + ". Revisá las cantidades disponibles.");
            }

            // Copia al pedido los datos y el precio del producto.
            PedidoProducto detalle = new PedidoProducto();
            detalle.setPedido(null); // Se asignará al pedido mediante la relación en cascada.
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());

            subtotal += cantidad * producto.getPrecio();
            detalles.add(detalle);
        }

        // Construye el pedido con los datos del usuario y del envío.
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setCarrito(carrito);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        // Guarda una copia de los datos de envío.
        pedido.setNombre(request.getNombre());
        pedido.setApellido(request.getApellido());
        pedido.setEmail(request.getEmail());
        pedido.setTelefono(request.getTelefono());
        pedido.setDireccion(request.getDireccion());
        pedido.setCiudad(request.getCiudad());
        pedido.setProvincia(request.getProvincia());
        pedido.setCodigoPostal(request.getCodigoPostal());

        // Establece la relación de cada detalle con el pedido.
        for (PedidoProducto detalle : detalles) {
            detalle.setPedido(pedido);
        }

        pedido.setProductos(detalles);
        pedido.setSubtotal(subtotal);

        // Por ahora el envío es gratuito, igual que en el resumen actual.
        double envio = 0.0;
        pedido.setEnvio(envio);
        pedido.setTotal(subtotal + envio);

        // Guarda el pedido y sus detalles dentro de la misma transacción.
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        return PedidoMapper.toDTO(pedidoGuardado);
    }

    // Obtiene los pedidos pertenecientes al usuario autenticado.
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarMisPedidos() {
        Usuario usuario = obtenerUsuarioAutenticado();

        return pedidoRepository.findByUsuario(usuario)
                .stream()
                .map(PedidoMapper::toDTO)
                .toList();
    }

    // Recupera al usuario autenticado desde el contexto de Spring Security.
    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof Usuario)) {
            throw new IllegalStateException(
                    "No se pudo identificar al usuario autenticado.");
        }

        return (Usuario) authentication.getPrincipal();
    }

    // Cancela un pedido pendiente y devuelve al inventario el stock reservado.
    @Transactional
    public void cancelarPedido(Integer pedidoId) {

        // Obtiene el usuario autenticado.
        Usuario usuario = obtenerUsuarioAutenticado();

        // Busca el pedido solicitado.
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe el pedido con ID: " + pedidoId));

        // Verifica que el pedido pertenezca al usuario autenticado.
        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No tenés permiso para cancelar este pedido.");
        }

        // Solo permite cancelar pedidos pendientes.
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden cancelar pedidos pendientes.");
        }

        // Devuelve al stock las cantidades reservadas de cada producto.
        for (PedidoProducto detalle : pedido.getProductos()) {

            int filasActualizadas = productoRepository.devolverStock(
                    detalle.getProducto().getId(),
                    detalle.getCantidad());

            // Si algún producto no puede actualizarse, revierte la operación.
            if (filasActualizadas != 1) {
                throw new IllegalStateException(
                        "No se pudo devolver el stock del producto ID: "
                        + detalle.getProducto().getId());
            }
        }

        // Marca el pedido como cancelado después de devolver el stock.
        pedido.setEstado(EstadoPedido.CANCELADO);

        // Guarda el nuevo estado.
        pedidoRepository.save(pedido);
    }
}
