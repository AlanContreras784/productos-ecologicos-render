package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para representar la información pública
 * de un usuario dentro de la API.
 *
 * Puede utilizarse en respuestas donde sea necesario
 * mostrar información del usuario asociado.
 *
 * Ejemplo de uso:
 *
 * - Información del usuario dentro del carrito.
 * - Respuestas de perfil.
 * - Datos relacionados con operaciones realizadas.
 *
 * ¿Por qué usamos un DTO?
 *
 * - Evita exponer directamente la entidad Usuario.
 * - Evita enviar información sensible como contraseñas.
 * - Permite controlar los datos visibles para el frontend.
 * - Mantiene separada la capa de persistencia de la presentación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Información pública de un usuario devuelta por la API."
)
public class UsuarioDTO {
    // Identificador único del usuario.
    //
    // Permite identificar al usuario dentro del sistema.
    @Schema(
            description = "Identificador del usuario",
            example = "1"
    )
    private Integer id;
    // Nombre de usuario utilizado para autenticación.
    //
    // Puede corresponder al username registrado.
    @Schema(
            description = "Nombre de usuario",
            example = "alan123"
    )
    private String username;
    // Nombre real del usuario.
    @Schema(
            description = "Nombre del usuario",
            example = "Alan"
    )
    private String nombre;
    // Apellido del usuario.
    @Schema(
            description = "Apellido del usuario",
            example = "Contreras"
    )
    private String apellido;
    // Rol asignado dentro del sistema.
    //
    // Ejemplos:
    // USER
    // ADMIN
    //
    // Se utiliza para determinar permisos
    // dentro de Spring Security.
    @Schema(
            description = "Rol del usuario dentro del sistema",
            example = "USER"
    )
    private String role;
}