package com.techlab.productos_ecologicos.auth;

import tools.jackson.databind.ObjectMapper;

import com.techlab.productos_ecologicos.dto.LoginResponseDTO;
import com.techlab.productos_ecologicos.jwt.JwtService;
import com.techlab.productos_ecologicos.models.Role;
import com.techlab.productos_ecologicos.models.Usuario;
import com.techlab.productos_ecologicos.repository.UsuarioRepository;
import com.techlab.productos_ecologicos.services.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Maneja el inicio de sesión mediante Google.
 *
 * Flujo:
 *
 * 1. Google autentica al usuario.
 * 2. Spring Security obtiene sus datos.
 * 3. Se obtiene el email de Google.
 * 4. Se busca el usuario en nuestra base de datos.
 * 5. Si no existe, se crea automáticamente.
 * 6. Si es un usuario nuevo, se notifica a Cero Huella.
 * 7. Se genera nuestro JWT.
 * 8. Se construye el mismo LoginResponseDTO utilizado
 *    por el login tradicional.
 * 9. Se redirige al frontend enviando la respuesta
 *    mediante el fragmento de la URL.
 *
 * Google solamente se utiliza para verificar la identidad.
 * La aplicación continúa utilizando JWT para autenticar
 * las peticiones posteriores.
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    // Servicio encargado de enviar la notificación
    // cuando se registra un usuario nuevo mediante Google.
    private final EmailService emailService;

    /**
     * Se ejecuta cuando Google autentica correctamente
     * al usuario.
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        // ======================================================
        // OBTENER DATOS DE GOOGLE
        // ======================================================

        OAuth2User oauth2User =
                (OAuth2User) authentication.getPrincipal();

        // Obtenemos el email de la cuenta Google.
        String email =
                oauth2User.getAttribute("email");

        // Verificamos que Google haya proporcionado un email.
        if (email == null || email.isBlank()) {

            throw new IllegalStateException(
                    "Google no proporcionó una dirección de email.");
        }

        // ======================================================
        // BUSCAR O CREAR USUARIO
        // ======================================================

        /*
         * Si el usuario ya existe:
         *
         *      se utiliza el usuario existente.
         *
         * Si no existe:
         *
         *      se crea mediante crearUsuarioGoogle().
         *
         * El método crearUsuarioGoogle() también se encarga
         * de generar el password temporal encriptado y de
         * enviar la notificación a Cero Huella.
         */
        Usuario usuario =
                usuarioRepository
                        .findByEmail(email)
                        .orElseGet(() ->
                                crearUsuarioGoogle(
                                        oauth2User,
                                        email));

        // ======================================================
        // GENERAR JWT
        // ======================================================

        String token =
                jwtService.generarToken(usuario);

        // ======================================================
        // CONSTRUIR RESPUESTA
        // ======================================================

        /*
         * Utilizamos exactamente el mismo DTO
         * que utiliza el login tradicional.
         *
         * Resultado:
         *
         * {
         *     "token": "...",
         *     "username": "...",
         *     "role": "USER"
         * }
         */
        LoginResponseDTO loginResponse =
                new LoginResponseDTO(
                        token,
                        usuario.getUsername(),
                        usuario.getRole().name());

        // Convertimos el DTO a JSON.
        String json =
                objectMapper.writeValueAsString(
                        loginResponse);

        // ======================================================
        // CODIFICAR RESPUESTA
        // ======================================================

        /*
         * Codificamos el JSON para poder enviarlo
         * correctamente dentro de la URL.
         */
        String respuestaCodificada =
                URLEncoder.encode(
                        json,
                        StandardCharsets.UTF_8);

        // ======================================================
        // REDIRECCIÓN AL FRONTEND
        // ======================================================

        /*
         * El fragmento (#) queda del lado del navegador.
         *
         * El backend no recibe nuevamente esta información
         * cuando el navegador procesa la URL.
         *
         * login.js podrá obtenerlo mediante:
         *
         * window.location.hash
         */
        String frontendUrl =
                "http://127.0.0.1:5500/pages/login.html"
                        + "#oauth2="
                        + respuestaCodificada;

        response.sendRedirect(frontendUrl);
    }

    /**
     * Crea un usuario local cuando una persona
     * inicia sesión por primera vez con Google.
     */
    private Usuario crearUsuarioGoogle(
            OAuth2User oauth2User,
            String email) {

        // ======================================================
        // DATOS DE GOOGLE
        // ======================================================

        String nombre =
                oauth2User.getAttribute("given_name");

        String apellido =
                oauth2User.getAttribute("family_name");

        // ======================================================
        // GENERAR USERNAME
        // ======================================================

        /*
         * Google no proporciona necesariamente
         * un username compatible con nuestro sistema.
         *
         * Por eso generamos uno a partir del email.
         */
        String username =
                generarUsername(email);

        // ======================================================
        // PASSWORD TEMPORAL
        // ======================================================

        /*
         * Nuestra entidad Usuario requiere que password
         * tenga un valor.
         *
         * El usuario de Google no utilizará esta contraseña
         * para iniciar sesión mediante Google.
         *
         * Se genera únicamente para cumplir con la estructura
         * de nuestra entidad y se almacena encriptada.
         */
        String passwordTemporal =
                UUID.randomUUID().toString();

        // ======================================================
        // CREAR USUARIO
        // ======================================================

        Usuario usuario =
                Usuario.builder()
                        .username(username)
                        .email(email)
                        .password(
                                passwordEncoder.encode(
                                        passwordTemporal))
                        .nombre(nombre)
                        .apellido(apellido)
                        .role(Role.USER)

                        /*
                         * Google ya verificó la identidad
                         * y el email de la cuenta.
                         */
                        .enabled(true)

                        .build();

        // ======================================================
        // GUARDAR USUARIO
        // ======================================================

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        // ======================================================
        // NOTIFICAR A CERO HUELLA
        // ======================================================

        /*
         * Solamente se envía la notificación cuando
         * realmente se creó un usuario nuevo.
         */
        emailService.enviarNotificacionNuevoRegistro(
                usuarioGuardado.getUsername(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getApellido());

        return usuarioGuardado;
    }

    /**
     * Genera un username único a partir del email.
     *
     * Ejemplo:
     *
     * alancontreras784@gmail.com
     *
     * se convierte inicialmente en:
     *
     * alancontreras784
     *
     * Si ya existe:
     *
     * alancontreras7841
     * alancontreras7842
     * etc.
     */
    private String generarUsername(String email) {

        String base =
                email.substring(
                        0,
                        email.indexOf("@"));

        String username = base;

        int contador = 1;

        while (usuarioRepository.existsByUsername(username)) {

            username =
                    base + contador;

            contador++;
        }

        return username;
    }
}