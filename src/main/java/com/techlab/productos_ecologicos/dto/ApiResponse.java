package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta genérica utilizada por toda la API.
 *
 * Permite devolver siempre la misma estructura al frontend,
 * independientemente del endpoint utilizado.
 *
 * Ejemplos de uso:
 *
 * ApiResponse<ProductoResponseDTO>
 * ApiResponse<List<ProductoResponseDTO>>
 * ApiResponse<CategoriaDTO>
 * ApiResponse<CarritoResumenDTO>
 *
 * ¿Por qué usamos un tipo genérico?
 *
 * - Permite reutilizar la misma estructura para cualquier entidad.
 * - Evita crear múltiples clases de respuesta.
 * - Mantiene un contrato uniforme entre backend y frontend.
 * - Facilita el manejo de respuestas en React.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Estructura estándar de respuesta utilizada por la API."
)
public class ApiResponse<T> {
    // Indica si la operación fue exitosa.
    //
    // En respuestas correctas normalmente será true.
    @Schema(
            description = "Indica si la operación fue exitosa",
            example = "true"
    )
    private boolean success;
    // Mensaje descriptivo de la operación realizada.
    //
    // Puede informar:
    // - Creación exitosa.
    // - Actualización realizada.
    // - Eliminación correcta.
    @Schema(
            description = "Mensaje descriptivo de la operación",
            example = "Producto creado correctamente"
    )
    private String message;
    // Información devuelta por la operación.
    //
    // El tipo depende del endpoint.
    //
    // Ejemplos:
    // ProductoResponseDTO
    // CategoriaDTO
    // List<ProductoResponseDTO>
    @Schema(
            description = "Datos devueltos por la operación"
    )
    private T data;
}