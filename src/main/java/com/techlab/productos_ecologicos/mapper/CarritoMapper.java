package com.techlab.productos_ecologicos.mapper;

import com.techlab.productos_ecologicos.dto.*;
import com.techlab.productos_ecologicos.models.Carrito;
import com.techlab.productos_ecologicos.models.CarritoProducto;

import java.util.stream.Collectors;

public class CarritoMapper {
    public static CarritoResponseDTO toDTO(Carrito carrito) {
        CarritoResponseDTO dto = new CarritoResponseDTO();
        dto.setId(carrito.getId());
        dto.setEstado(carrito.getEstado().name());

        // Usuario
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(carrito.getUsuario().getId());
        usuarioDTO.setUsername(carrito.getUsuario().getUsername());
        usuarioDTO.setNombre(carrito.getUsuario().getNombre());
        usuarioDTO.setApellido(carrito.getUsuario().getApellido());
        usuarioDTO.setRole(carrito.getUsuario().getRole().name());
        dto.setUsuario(usuarioDTO);
        // Productos del carrito
        dto.setProductos(
                carrito.getProductos()
                        .stream()
                        .map(CarritoMapper::mapCarritoProducto)
                        .collect(Collectors.toList())
        );

        return dto;
    }
    private static CarritoProductoDTO mapCarritoProducto(CarritoProducto carritoProducto) {
        CarritoProductoDTO dto = new CarritoProductoDTO();
        dto.setId(carritoProducto.getId());
        dto.setCantidad(carritoProducto.getCantidad());

        ProductoCarritoDTO productoDTO = new ProductoCarritoDTO();
        productoDTO.setId(carritoProducto.getProducto().getId());
        productoDTO.setNombre(carritoProducto.getProducto().getNombre());
        productoDTO.setPrecio(carritoProducto.getProducto().getPrecio());
        productoDTO.setImagenUrl(carritoProducto.getProducto().getImagenUrl());

        dto.setProducto(productoDTO);

        return dto;
    }
}
