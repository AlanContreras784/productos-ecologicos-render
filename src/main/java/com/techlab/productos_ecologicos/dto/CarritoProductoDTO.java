package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para representar un producto dentro de un carrito.
 *
 * Contiene la información de la relación entre:
 *
 * - Un carrito.
 * - Un producto.
 * - La cantidad seleccionada por el usuario.
 *
 * Se utiliza para enviar información del carrito al frontend
 * sin exponer directamente la entidad CarritoProducto.
 *
 * ¿Por qué usamos un DTO?
 *
 * - Evita exponer entidades JPA directamente.
 * - Permite controlar la información que recibe el frontend.
 * - Facilita modificar la estructura interna del carrito.
 * - Permite incluir información resumida del producto asociado.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Información de un producto agregado dentro de un carrito."
)
public class CarritoProductoDTO {
    // Identificador único del registro carrito-producto.
    //
    // Representa la relación entre un producto y un carrito.
    @Schema(
            description = "Identificador del producto dentro del carrito",
            example = "1"
    )
    private Integer id;
    // Cantidad seleccionada por el usuario.
    //
    // Indica cuántas unidades de este producto
    // forman parte del carrito.
    @Schema(
            description = "Cantidad de unidades agregadas al carrito",
            example = "2"
    )
    private Integer cantidad;
    // Información del producto asociado.
    //
    // Se utiliza ProductoCarritoDTO para evitar enviar
    // la entidad Producto completa y controlar la información
    // visible para el frontend.
    @Schema(
            description = "Información resumida del producto agregado al carrito"
    )
    private ProductoCarritoDTO producto;
}