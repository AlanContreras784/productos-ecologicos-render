package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta devuelta después de registrar un usuario.
 *
 * A diferencia del login, el registro no genera
 * una sesión JWT porque la cuenta debe confirmar
 * primero su dirección de email.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Respuesta generada después de registrar un usuario."
)
public class RegistroResponseDTO {

    @Schema(
            description = "Nombre de usuario registrado",
            example = "alan123"
    )
    private String username;

    @Schema(
            description = "Correo electrónico registrado",
            example = "alan@gmail.com"
    )
    private String email;

    @Schema(
            description = "Mensaje informativo sobre el registro",
            example = "Registro exitoso. Revisa tu correo para confirmar la cuenta."
    )
    private String mensaje;
}