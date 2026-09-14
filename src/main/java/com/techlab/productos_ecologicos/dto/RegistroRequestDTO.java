package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO preparado para registrar nuevos usuarios.
 *
 * Actualmente solo debe utilizarse si existe
 * un endpoint de registro.
 *
 * Ejemplo:
 *
 * POST /auth/register
 *
 * No devuelve información del usuario.
 * Solamente transporta los datos necesarios
 * para crearlo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Datos necesarios para registrar un nuevo usuario."
)
public class RegistroRequestDTO {
    // Usuario que utilizará el sistema.
    @Schema(
            description = "Nombre de usuario",
            example = "usuarioPrueba01",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El username no puede estar vacío")
    private String username;
    // Email utilizado para confirmar la cuenta.
    @Schema(
            description = "Correo electrónico del usuario",
            example = "alan@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    // Nombre real del usuario.
    @Schema(
            description = "Nombre del usuario",
            example = "Alan",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    // Apellido del usuario.
    @Schema(
            description = "Apellido del usuario",
            example = "Contreras",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;
    // Contraseña inicial.
    //
    // Será encriptada antes de guardarse.
    @Schema(
            description = "Contraseña del usuario",
            example = "Prueba123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}