package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para representar la información resumida
 * de un producto dentro del carrito.
 *
 * Se utiliza dentro de:
 *
 * CarritoProductoDTO
 *
 * ¿Por qué usamos un DTO específico?
 *
 * - Evita enviar toda la información del producto.
 * - Reduce el tamaño de la respuesta JSON.
 * - Evita exponer datos que no son necesarios en el carrito.
 * - Facilita adaptar la información mostrada al usuario.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Información resumida de un producto dentro del carrito."
)
public class ProductoCarritoDTO {
    // Identificador único del producto.
    //
    // Permite identificar el producto
    // para operaciones como aumentar o eliminar cantidad.
    @Schema(
            description = "Identificador del producto",
            example = "1"
    )
    private Integer id;
    // Nombre comercial del producto.
    //
    // Es utilizado para mostrar la descripción
    // principal del producto en el carrito.
    @Schema(
            description = "Nombre del producto",
            example = "Jabón ecológico artesanal"
    )
    private String nombre;
    // Precio actual del producto.
    //
    // Permite calcular subtotales y mostrar
    // el valor unitario al usuario.
    @Schema(
            description = "Precio unitario del producto",
            example = "850.50"
    )
    private Double precio;
    // Imagen asociada al producto.
    //
    // Se utiliza para mostrar una vista previa
    // del producto en el carrito.
    @Schema(
            description = "URL de la imagen del producto",
            example = "https://misitio.com/imagenes/jabon.jpg"
    )
    private String imagenUrl;
}