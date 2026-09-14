package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para enviar la información de un producto al frontend.
 *
 * Se utiliza en las respuestas de los endpoints GET, POST y PUT.
 *
 * ¿Por qué usamos un ResponseDTO?
 * - Evita exponer directamente la entidad Producto.
 * - Permite enviar únicamente la información necesaria al cliente.
 * - Facilita modificar la estructura de la API sin afectar la base de datos.
 * - Puede incluir información de otras entidades, como la categoría,
 *   sin exponer relaciones JPA.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información de un producto devuelta por la API.")
public class ProductoResponseDTO {
    // Identificador único del producto.
    @Schema(
            description = "Identificador del producto",
            example = "1"
    )
    private Integer id;
    // Nombre del producto.
    @Schema(
            description = "Nombre del producto",
            example = "Jabón ecológico artesanal"
    )
    private String nombre;
    // Precio del producto.
    @Schema(
            description = "Precio del producto",
            example = "850.50"
    )
    private Double precio;
    // Descripción del producto.
    @Schema(
            description = "Descripción del producto",
            example = "Jabón biodegradable elaborado con ingredientes naturales."
    )
    private String descripcion;
    // Cantidad disponible.
    @Schema(
            description = "Cantidad disponible en stock",
            example = "30"
    )
    private Integer stock;
    // Imagen del producto.
    @Schema(
            description = "URL de la imagen",
            example = "https://misitio.com/imagenes/jabon.jpg"
    )
    private String imagenUrl;


    @Schema(
        description = "Indica si el producto está marcado como destacado",
        example = "true"
    )
    private Boolean destacado;
    // Información de la categoría.
    //
    // A diferencia del RequestDTO, aquí devolvemos un CategoriaDTO
    // completo para que el frontend pueda mostrar el nombre y la
    // descripción sin realizar otra consulta.
    @Schema(description = "Categoría a la que pertenece el producto")
    private CategoriaDTO categoria;
}