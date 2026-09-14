package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para recibir la información necesaria
 * para crear o actualizar una categoría.
 *
 * Se utiliza en los endpoints:
 *
 * POST /categorias
 * PUT  /categorias/{id}
 *
 * ¿Por qué usamos un RequestDTO?
 *
 * - Evita exponer directamente la entidad Categoria.
 * - Permite validar los datos antes de llegar al Service.
 * - El frontend solamente envía la información necesaria.
 * - Mantiene separada la capa de presentación de la capa de persistencia.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Datos necesarios para crear o actualizar una categoría de productos."
)
public class CategoriaRequestDTO {
    // Nombre que tendrá la categoría.
    // No puede enviarse vacío.
    @Schema(
            description = "Nombre de la categoría",
            example = "Productos reutilizables",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    private String nombre;
    // Descripción de la categoría.
    // Permite informar al usuario sobre el tipo de productos incluidos.
    @Schema(
            description = "Descripción de la categoría",
            example = "Productos ecológicos diseñados para reducir residuos.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La descripción de la categoría no puede estar vacía")
    private String descripcion;
}