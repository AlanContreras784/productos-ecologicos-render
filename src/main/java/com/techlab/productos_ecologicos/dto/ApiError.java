package com.techlab.productos_ecologicos.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para representar errores generados por la API.
 *
 * Se utiliza principalmente en conjunto con:
 *
 * - GlobalExceptionHandler.
 * - Respuestas HTTP de error.
 *
 * Permite devolver una estructura uniforme al frontend
 * cuando ocurre una excepción.
 *
 * ¿Por qué usamos un DTO de error?
 *
 * - Mantiene un formato estándar para todos los errores.
 * - Facilita el manejo de errores en el frontend.
 * - Evita exponer detalles internos de la aplicación.
 * - Permite incluir errores específicos de validación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Información detallada de un error generado por la API."
)
public class ApiError {
    // Indica si la operación fue exitosa.
    //
    // En errores normalmente tendrá valor false.
    @Schema(
            description = "Indica si la operación fue exitosa",
            example = "false"
    )
    private boolean success;
    // Código HTTP correspondiente al error.
    //
    // Ejemplos:
    // 400 BAD_REQUEST
    // 404 NOT_FOUND
    // 500 INTERNAL_SERVER_ERROR
    @Schema(
            description = "Código HTTP del error",
            example = "404"
    )
    private int status;
    // Nombre del estado HTTP.
    //
    // Ejemplo:
    // NOT_FOUND
    // BAD_REQUEST
    @Schema(
            description = "Nombre del estado HTTP asociado al error",
            example = "NOT_FOUND"
    )
    private String error;
    // Mensaje descriptivo para informar al cliente
    // qué ocurrió.
    @Schema(
            description = "Mensaje descriptivo del error",
            example = "El producto solicitado no existe"
    )
    private String message;
    // Información adicional del error.
    //
    // Puede contener:
    // - Errores de validación.
    // - Campos inválidos.
    // - Información complementaria.
    @Schema(
            description = "Información adicional del error, como errores de validación",
            example = "{\"nombre\":\"El nombre no puede estar vacío\"}"
    )
    private Object errors;
    // Fecha y hora en la que ocurrió el error.
    @Schema(
            description = "Fecha y hora en la que ocurrió el error",
            example = "2026-07-20T23:30:00"
    )
    private LocalDateTime timestamp;
}