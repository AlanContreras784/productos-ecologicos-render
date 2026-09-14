package com.techlab.productos_ecologicos.mapper;


import java.util.List;
import java.util.stream.Collectors;

import com.techlab.productos_ecologicos.dto.CategoriaDTO;
import com.techlab.productos_ecologicos.dto.ProductoRequestDTO;
import com.techlab.productos_ecologicos.dto.ProductoResponseDTO;
import com.techlab.productos_ecologicos.models.Categoria;
import com.techlab.productos_ecologicos.models.Producto;


public class ProductoMapper {
    // Convierte una entidad Producto en un ProductoResponseDTO.
    // Se utiliza para enviar información al frontend sin exponer la entidad JPA.
    public static ProductoResponseDTO toDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setDescripcion(producto.getDescripcion());
        dto.setStock(producto.getStock());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setDestacado(producto.getDestacado());
        // Convierte la categoría del producto a CategoriaDTO.
        if (producto.getCategoria() != null) {
            CategoriaDTO categoriaDTO = new CategoriaDTO();
            categoriaDTO.setId(producto.getCategoria().getId());
            categoriaDTO.setNombre(producto.getCategoria().getNombre());
            categoriaDTO.setDescripcion(producto.getCategoria().getDescripcion());

            dto.setCategoria(categoriaDTO);
        }
        return dto;
    }

    // Convierte una lista de entidades Producto en una lista de ProductoResponseDTO.
    public static List<ProductoResponseDTO> toDTOList(List<Producto> productos) {
        return productos.stream()
                .map(ProductoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Convierte un ProductoRequestDTO recibido desde el frontend
    // en una entidad Producto lista para guardar.
    //
    // La categoría se recibe como parámetro porque el Mapper
    // no debe acceder al Repository.
    public static Producto toEntity(
            ProductoRequestDTO dto,
            Categoria categoria) {

        Producto producto = new Producto();

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setDestacado(dto.getDestacado());
        producto.setCategoria(categoria);
        return producto;
    }

    // Actualiza una entidad Producto existente utilizando los datos
    // enviados desde ProductoRequestDTO.
    //
    // Se utiliza para PUT porque no queremos crear un objeto nuevo,
    // sino modificar el producto que ya existe en la base de datos.
    public static void updateEntity(
            Producto producto,
            ProductoRequestDTO dto,
            Categoria categoria) {

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setDestacado(dto.getDestacado());
        producto.setCategoria(categoria);
    }
}