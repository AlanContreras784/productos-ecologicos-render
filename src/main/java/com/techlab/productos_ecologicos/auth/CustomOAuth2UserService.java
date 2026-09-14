package com.techlab.productos_ecologicos.auth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * Servicio encargado de obtener y procesar
 * la información del usuario autenticado mediante Google.
 *
 * Responsabilidades:
 *
 * - Obtener los datos proporcionados por Google.
 * - Verificar que Google proporcione un email.
 * - Devolver el OAuth2User a Spring Security.
 *
 * La búsqueda y creación del usuario en nuestra base de datos
 * NO se realiza aquí.
 *
 * Esa responsabilidad pertenece a:
 *
 * OAuth2AuthenticationSuccessHandler
 *
 * De esta manera evitamos duplicar la lógica de creación
 * de usuarios y envío de notificaciones.
 */
@Service
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    /**
     * Procesa la información proporcionada por Google
     * mediante OAuth2.
     *
     * @param userRequest información de la autenticación OAuth2.
     * @return usuario OAuth2 autenticado.
     */
    @Override
    public OAuth2User loadUser(
            OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        // Utilizamos el servicio estándar de Spring Security
        // para obtener los datos del usuario desde Google.
        DefaultOAuth2UserService delegate =
                new DefaultOAuth2UserService();

        OAuth2User oauth2User =
                delegate.loadUser(userRequest);

        // Obtenemos los atributos proporcionados por Google.
        Map<String, Object> attributes =
                oauth2User.getAttributes();

        // Obtenemos el email.
        String email =
                (String) attributes.get("email");

        // Verificamos que Google haya proporcionado
        // una dirección de email.
        if (email == null || email.isBlank()) {

            throw new OAuth2AuthenticationException(
                    new OAuth2Error("email_not_found"),
                    "Google no proporcionó una dirección de email.");
        }

        /*
         * OAuth2 necesita identificar cuál atributo utilizar
         * como nombre del usuario.
         *
         * Google proporciona "email", por lo que lo utilizamos
         * como identificador principal.
         */
        return new DefaultOAuth2User(
                Collections.singleton(
                        new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                "ROLE_USER")),
                attributes,
                "email");
    }
}