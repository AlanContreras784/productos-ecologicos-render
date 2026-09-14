package com.techlab.productos_ecologicos.repository;

import com.techlab.productos_ecologicos.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

   // Spring Security necesita buscar el usuario por username para validar credenciales.
    // JPA genera la query automáticamente a partir del nombre del método.
    Optional<Usuario> findByUsername(String username);

    // Permite buscar una cuenta por email.
    // Será utilizado para:
    // - Confirmación de email.
    // - Login con Google.
    Optional<Usuario> findByEmail(String email);

    // Comprueba si ya existe una cuenta utilizando ese email.
    // Evita registrar dos usuarios con el mismo correo.
    boolean existsByEmail(String email);

    // Comprueba si ya existe una cuenta utilizando ese username.
    // Permite controlar duplicados antes de registrar.
    boolean existsByUsername(String username);
}