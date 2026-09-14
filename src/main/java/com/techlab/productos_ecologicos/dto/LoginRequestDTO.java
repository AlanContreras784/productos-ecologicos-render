package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO utilizado para recibir las credenciales
 * necesarias para iniciar sesión.
 *
 * Flujo:
 *
 * Frontend
 *    |
 *    ↓
 * LoginRequestDTO
 *    |
 *    ↓
 * AuthService
 *    |
 *    ↓
 * Spring Security
 *    |
 *    ↓
 * JWT
 *
 * No contiene lógica de autenticación.
 * Solamente transporta los datos enviados por el cliente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Datos necesarios para autenticarse en la API."
)
public class LoginRequestDTO {
    // Nombre de usuario utilizado para ingresar.
    @Schema(
            description = "Nombre de usuario",
            example = "admin",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El usuario no puede estar vacío")
    private String username;
    // Contraseña del usuario.
    //
    // Nunca se devuelve al frontend.
    @Schema(
            description = "Contraseña del usuario",
            example = "123456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}