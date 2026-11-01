package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para representar un producto
 * incluido dentro de un pedido.
 *
 * Guarda el precio utilizado al momento de la compra,
 * independientemente de cambios posteriores en el producto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Producto incluido dentro de un pedido.")
public class PedidoProductoResponseDTO {

    @Schema(description = "Identificador del detalle del pedido", example = "1")
    private Integer id;

    @Schema(description = "Identificador del producto", example = "5")
    private Integer productoId;

    @Schema(description = "Nombre del producto", example = "Botella reutilizable")
    private String nombre;

    @Schema(description = "URL de la imagen del producto")
    private String imagenUrl;

    @Schema(description = "Cantidad comprada", example = "2")
    private Integer cantidad;

    @Schema(
        description = "Precio unitario registrado al momento de la compra",
        example = "2500.00"
    )
    private Double precioUnitario;

    @Schema(
        description = "Subtotal correspondiente a este producto",
        example = "5000.00"
    )
    private Double subtotal;
}