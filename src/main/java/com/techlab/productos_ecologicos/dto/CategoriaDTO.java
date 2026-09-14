package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para representar la información de una categoría.
 *
 * Se utiliza para enviar información de categorías desde la API
 * hacia el frontend.
 *
 * ¿Por qué usamos un DTO?
 *
 * - Evita exponer directamente la entidad Categoria.
 * - Permite controlar qué información recibe el frontend.
 * - Facilita modificar la estructura interna de la aplicación
 *   sin afectar el contrato de la API.
 * - Mantiene separada la capa de persistencia de la capa de presentación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Información de una categoría de productos devuelta por la API."
)
public class CategoriaDTO {
    // Identificador único de la categoría.
    //
    // Permite identificar la categoría
    // dentro de la base de datos.
    @Schema(
            description = "Identificador de la categoría",
            example = "1"
    )
    private Integer id;
    // Nombre visible de la categoría.
    //
    // Ejemplo:
    // Productos reutilizables.
    @Schema(
            description = "Nombre de la categoría",
            example = "Productos reutilizables",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    private String nombre;
    // Descripción informativa de la categoría.
    //
    // Permite explicar al usuario
    // qué tipo de productos contiene.
    @Schema(
            description = "Descripción de la categoría",
            example = "Productos diseñados para reducir el impacto ambiental."
    )
    private String descripcion;
}