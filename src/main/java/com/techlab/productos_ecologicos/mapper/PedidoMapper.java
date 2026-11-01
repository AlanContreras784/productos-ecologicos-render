
package com.techlab.productos_ecologicos.mapper;

import java.util.List;

import com.techlab.productos_ecologicos.dto.PedidoProductoResponseDTO;
import com.techlab.productos_ecologicos.dto.PedidoResponseDTO;
import com.techlab.productos_ecologicos.models.Pedido;
import com.techlab.productos_ecologicos.models.PedidoProducto;

public class PedidoMapper {

    // Convierte un pedido y sus productos a un DTO de respuesta.
    public static PedidoResponseDTO toDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();

        dto.setId(pedido.getId());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado().name());

        // Datos de envío.
        dto.setNombre(pedido.getNombre());
        dto.setApellido(pedido.getApellido());
        dto.setEmail(pedido.getEmail());
        dto.setTelefono(pedido.getTelefono());
        dto.setDireccion(pedido.getDireccion());
        dto.setCiudad(pedido.getCiudad());
        dto.setProvincia(pedido.getProvincia());
        dto.setCodigoPostal(pedido.getCodigoPostal());

        // Importes calculados y guardados en el pedido.
        dto.setSubtotal(pedido.getSubtotal());
        dto.setEnvio(pedido.getEnvio());
        dto.setTotal(pedido.getTotal());

        // Productos incluidos en el pedido.
        List<PedidoProductoResponseDTO> productos = pedido.getProductos()
                .stream()
                .map(PedidoMapper::productoToDTO)
                .toList();

        dto.setProductos(productos);

        return dto;
    }

    // Convierte el detalle de un producto comprado a su DTO.
    private static PedidoProductoResponseDTO productoToDTO(
            PedidoProducto pedidoProducto) {

        PedidoProductoResponseDTO dto = new PedidoProductoResponseDTO();

        dto.setId(pedidoProducto.getId());
        dto.setProductoId(pedidoProducto.getProducto().getId());
        dto.setNombre(pedidoProducto.getProducto().getNombre());
        dto.setImagenUrl(pedidoProducto.getProducto().getImagenUrl());
        dto.setCantidad(pedidoProducto.getCantidad());
        dto.setPrecioUnitario(pedidoProducto.getPrecioUnitario());

        // El subtotal usa el precio histórico de la compra,
        // no el precio actual del catálogo.
        dto.setSubtotal(
                pedidoProducto.getCantidad()
                        * pedidoProducto.getPrecioUnitario()
        );

        return dto;
    }
}
