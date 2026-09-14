package com.techlab.productos_ecologicos.services;

import com.techlab.productos_ecologicos.dto.ProductoRequestDTO;
import com.techlab.productos_ecologicos.models.Categoria;
import com.techlab.productos_ecologicos.repository.CategoriaRepository;
import com.techlab.productos_ecologicos.dto.ProductoResponseDTO;
import com.techlab.productos_ecologicos.mapper.ProductoMapper;
import com.techlab.productos_ecologicos.exception.CategoriaNoEncontradaException;
import com.techlab.productos_ecologicos.exception.ProductoNoEncontradoException;
import com.techlab.productos_ecologicos.repository.ProductoRepository;
import com.techlab.productos_ecologicos.models.Producto;

import java.util.List;

import org.springframework.stereotype.Service;


 
@Service 
public class ProductoService {
    
    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService( ProductoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }
    public Producto guardar(Producto p) {       
        return repository.save(p);
    }
    public List<Producto> listarTodos() {
        return repository.findAll();
    }
    public Producto obtenerPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> 
        new ProductoNoEncontradoException("No se encontró un producto con id " + id));
    }
    private Producto actualizar(Integer id, Producto datos) {
        Producto p = obtenerPorId(id);

        p.setNombre(datos.getNombre());
        p.setPrecio(datos.getPrecio());
        p.setStock(datos.getStock());
        p.setCategoria(datos.getCategoria());
        p.setDescripcion(datos.getDescripcion());
        if (datos.getImagenUrl() != null) {
            p.setImagenUrl(datos.getImagenUrl());
        }   
        return repository.save(p);
    }
    public void eliminar(Integer id) {
        Producto p = obtenerPorId(id);
        repository.delete(p);
    }
    public List<Producto> buscarPorNombre(String nombre) {
        return repository.findByNombreContaining(nombre);
    }
    public List<Producto> buscarPorCategoria(String categoria) {
        return repository.findByCategoriaNombreContainingIgnoreCase(categoria);
    }
    // Obtiene todos los productos y los convierte a DTO para enviarlos al frontend.
    public List<ProductoResponseDTO> obtenerProductosResponse() {
        return ProductoMapper.toDTOList(listarTodos());
    }
    // Obtiene solamente los productos marcados como destacados
    // y los convierte a ProductoResponseDTO para enviarlos al frontend.
    public List<ProductoResponseDTO> obtenerProductosDestacados() {
        return ProductoMapper.toDTOList(repository.findByDestacadoTrue());
    }
    // Obtiene un producto por id y lo convierte a DTO para enviarlo al frontend.
    public ProductoResponseDTO obtenerProductoResponse(Integer id) {
        Producto producto = obtenerPorId(id);
        return ProductoMapper.toDTO(producto);
    }
    // Busca productos por nombre y convierte el resultado a DTO para el frontend.
    public List<ProductoResponseDTO> buscarPorNombreResponse(String nombre) {
        return ProductoMapper.toDTOList(
                buscarPorNombre(nombre)
        );
    }
    // Busca productos por categoría y convierte el resultado a DTO para el frontend.
    public List<ProductoResponseDTO> buscarPorCategoriaResponse(String categoria) {
        return ProductoMapper.toDTOList(
                buscarPorCategoria(categoria)
        );
    }
    // Guarda un producto y devuelve la información preparada para el frontend.
    public ProductoResponseDTO guardarResponse(Producto producto) {
        Producto guardado = repository.save(producto);
        return ProductoMapper.toDTO(guardado);
    }
    // Actualiza un producto existente y devuelve un DTO con los datos actualizados.
    public ProductoResponseDTO actualizarResponse(Integer id, Producto datos) {
        Producto actualizado = actualizar(id, datos);
        return ProductoMapper.toDTO(actualizado);
    }
    // Busca una categoría por su id.
    // Se utiliza cuando el frontend envía solamente categoriaId.
    private Categoria obtenerCategoria(Integer categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() ->
                    new CategoriaNoEncontradaException(
                        "No se encontró la categoría con id " + categoriaId
                    ));
    }
    // Crea un producto utilizando ProductoRequestDTO.
    // Convierte el DTO en entidad mediante el Mapper
    // y luego guarda en la base de datos.
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        Categoria categoria = obtenerCategoria(dto.getCategoriaId());
        Producto producto = ProductoMapper.toEntity(
                dto,
                categoria
        );
        Producto guardado = repository.save(producto);
        return ProductoMapper.toDTO(guardado);
    }

    // Actualiza un producto existente utilizando ProductoRequestDTO.
    // Busca el producto existente, actualiza sus datos
    // y devuelve un DTO para el frontend.
    public ProductoResponseDTO actualizarProducto(
            Integer id,
            ProductoRequestDTO dto) {
        Producto producto = obtenerPorId(id);
        Categoria categoria = obtenerCategoria(
                dto.getCategoriaId()
        );
        ProductoMapper.updateEntity(
                producto,
                dto,
                categoria
        );
        Producto actualizado = repository.save(producto);
        return ProductoMapper.toDTO(actualizado);
    }
}

