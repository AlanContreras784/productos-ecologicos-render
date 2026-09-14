package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO utilizado para devolver la respuesta
 * luego de una autenticación exitosa.
 *
 * Contiene el token JWT que deberá enviar
 * el frontend en las siguientes peticiones protegidas.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Respuesta generada luego de autenticar correctamente un usuario."
)
public class LoginResponseDTO {
    // Token JWT generado por Spring Security.
    //
    // El frontend debe enviarlo en:
    //
    // Authorization: Bearer TOKEN
    //
    @Schema(
            description = "Token JWT para acceder a endpoints protegidos",
            example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    private String token;
    // Nombre del usuario autenticado.
    @Schema(
            description = "Nombre del usuario autenticado",
            example = "admin"
    )
    private String username;
    // Rol asignado al usuario.
    //
    // Ejemplo:
    // ADMIN
    // USER
    //
    // Permite al frontend adaptar permisos visuales.
    @Schema(
            description = "Rol del usuario autenticado",
            example = "ADMIN"
    )
    private String role;
}
