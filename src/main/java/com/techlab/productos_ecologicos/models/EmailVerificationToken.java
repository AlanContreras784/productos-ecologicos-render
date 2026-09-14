package com.techlab.productos_ecologicos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa un token utilizado para confirmar
 * el correo electrónico de un usuario.
 *
 * El token es independiente del JWT utilizado
 * para autenticar las peticiones de la API.
 *
 * Su única responsabilidad es permitir que un usuario
 * confirme su dirección de correo electrónico.
 */
@Entity
@Table(
    name = "email_verification_token",
    indexes = {
        @Index(
            name = "idx_email_verification_token",
            columnList = "token"
        )
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Token único enviado al usuario mediante email.
    @Column(nullable = false, unique = true)
    private String token;

    // Usuario asociado al token de confirmación.
    //
    // Un usuario puede tener un token activo
    // de confirmación.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "usuario_id",
        nullable = false,
        unique = true
    )
    private Usuario usuario;

    // Fecha y hora en la que el token deja de ser válido.
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;
}