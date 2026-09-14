package com.techlab.productos_ecologicos.repository;

import com.techlab.productos_ecologicos.models.EmailVerificationToken;
import com.techlab.productos_ecologicos.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Integer> {

    // Busca un token específico.
    //
    // Será utilizado cuando el usuario haga clic
    // en el enlace de confirmación recibido por email.
    Optional<EmailVerificationToken> findByToken(String token);

    // Busca el token asociado a un usuario.
    //
    // Permite comprobar si el usuario ya tiene
    // un token de confirmación activo.
    Optional<EmailVerificationToken> findByUsuario(Usuario usuario);

    // Permite eliminar el token asociado a un usuario.
    //
    // Se utilizará después de confirmar correctamente
    // el correo electrónico.
    void deleteByUsuario(Usuario usuario);
}