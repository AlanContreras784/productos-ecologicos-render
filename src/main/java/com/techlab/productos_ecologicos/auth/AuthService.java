package com.techlab.productos_ecologicos.auth;

import com.techlab.productos_ecologicos.dto.LoginRequestDTO;
import com.techlab.productos_ecologicos.dto.LoginResponseDTO;
import com.techlab.productos_ecologicos.dto.RegistroRequestDTO;
import com.techlab.productos_ecologicos.dto.RegistroResponseDTO;
import com.techlab.productos_ecologicos.jwt.JwtService;
import com.techlab.productos_ecologicos.models.EmailVerificationToken;
import com.techlab.productos_ecologicos.models.Role;
import com.techlab.productos_ecologicos.models.Usuario;
import com.techlab.productos_ecologicos.repository.EmailVerificationTokenRepository;
import com.techlab.productos_ecologicos.repository.UsuarioRepository;
import com.techlab.productos_ecologicos.services.EmailService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
        
/**
 * Servicio encargado de la autenticación y registro de usuarios.
 *
 * Responsabilidades:
 *
 * - Validar credenciales mediante Spring Security.
 * - Crear usuarios nuevos.
 * - Encriptar contraseñas con BCrypt.
 * - Generar tokens JWT para login.
 * - Generar tokens de confirmación de email durante el registro.
 *
 * No maneja respuestas HTTP.
 * Esa responsabilidad pertenece al AuthController.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

        private final UsuarioRepository usuarioRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        // Repository encargado de guardar y buscar
        // los tokens de confirmación de email.
        private final EmailVerificationTokenRepository emailVerificationTokenRepository;

        // Servicio encargado del envío del correo.
        private final EmailService emailService;

        /**
         * Autentica un usuario existente.
         *
         * Flujo:
         *
         * 1) Recibe username y password.
         * 2) Spring Security valida las credenciales.
         * 3) Busca el usuario autenticado.
         * 4) Genera un JWT.
         * 5) Devuelve la información al Controller.
         */
        public LoginResponseDTO login(LoginRequestDTO request) {

                // Activa la cadena interna de Spring Security.
                //
                // Internamente:
                // - Busca el usuario por username.
                // - Aplica BCrypt.
                // - Comprueba que la cuenta esté habilitada.
                // - Compara la contraseña ingresada contra la almacenada.
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));

                // Recuperamos el usuario autenticado.
                Usuario usuario = usuarioRepository
                                .findByUsername(request.getUsername())
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "Usuario no encontrado."));

                // Generamos el token JWT.
                String token = jwtService.generarToken(usuario);

                // Devolvemos la respuesta para el frontend.
                return new LoginResponseDTO(
                                token,
                                usuario.getUsername(),
                                usuario.getRole().name());
        }

        /**
         * Registra un nuevo usuario.
         *
         * Flujo:
         *
         * 1) Recibe los datos del formulario.
         * 2) Crea el usuario con la cuenta deshabilitada.
         * 3) Encripta la contraseña.
         * 4) Guarda el usuario.
         * 5) Genera un token de confirmación.
         * 6) Guarda el token asociado al usuario.
         * 7) Prepara el envío del email.
         *
         * IMPORTANTE:
         *
         * No genera JWT.
         *
         * El usuario solamente podrá iniciar sesión
         * después de confirmar su dirección de email.
         */
        public RegistroResponseDTO register(
                        RegistroRequestDTO request) {

                // Comprobamos que el username no esté registrado.
                if (usuarioRepository.existsByUsername(
                                request.getUsername())) {

                        throw new IllegalArgumentException(
                                        "El username ya está registrado.");
                }

                // Comprobamos que el email no esté registrado.
                if (usuarioRepository.existsByEmail(
                                request.getEmail())) {

                        throw new IllegalArgumentException(
                                        "El email ya está registrado.");
                }

                // Construcción del usuario.
                //
                // La contraseña nunca se guarda en texto plano.
                // BCrypt genera un hash irreversible.
                //
                // enabled = false significa que el usuario
                // todavía no confirmó su correo.
                Usuario usuario = Usuario.builder()
                                .username(request.getUsername())
                                .email(request.getEmail())
                                .password(
                                                passwordEncoder.encode(
                                                                request.getPassword()))
                                .nombre(request.getNombre())
                                .apellido(request.getApellido())
                                .role(Role.USER)
                                .enabled(false)
                                .build();

                // Guardamos primero el usuario para obtener su ID.
                usuarioRepository.save(usuario);

                // Generamos un token aleatorio de confirmación.
                String token = UUID.randomUUID().toString();

                // El token tendrá una duración limitada.
                //
                // En esta primera implementación:
                // 24 horas.
                EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                                .token(token)
                                .usuario(usuario)
                                .fechaExpiracion(
                                                LocalDateTime.now().plusHours(24))
                                .build();

                // Guardamos el token.
                emailVerificationTokenRepository.save(
                                verificationToken);

                // Preparamos el envío del correo.
                //
                // Por ahora EmailService solamente registra
                // la información en la consola.
                // Enviamos el correo de confirmación al usuario.
                emailService.enviarEmailConfirmacion(
                                usuario.getEmail(),
                                token);
                // Enviamos una notificación a Cero Huella
                // informando que se registró un nuevo usuario.
                emailService.enviarNotificacionNuevoRegistro(
                        usuario.getUsername(),
                        usuario.getEmail(),
                        usuario.getNombre(),
                        usuario.getApellido()
                );

                // No se genera JWT.
                //
                // El usuario deberá confirmar primero
                // su dirección de correo.
                return new RegistroResponseDTO(
                                usuario.getUsername(),
                                usuario.getEmail(),
                                "Registro exitoso. Revisa tu correo para confirmar la cuenta.");
        }

        /**
         * Confirma la dirección de email de un usuario.
         *
         * Flujo:
         *
         * 1) Recibe el token enviado por email.
         * 2) Busca el token en la base de datos.
         * 3) Verifica que no haya expirado.
         * 4) Habilita la cuenta del usuario.
         * 5) Guarda los cambios.
         * 6) Elimina el token para impedir su reutilización.
         *
         * @param token token de confirmación recibido por email.
         */
        public void confirmarEmail(String token) {

                // Buscamos el token en la base de datos.
                EmailVerificationToken verificationToken = emailVerificationTokenRepository
                                .findByToken(token)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "El token de confirmación no es válido."));

                // Verificamos si el token ya expiró.
                if (verificationToken.getFechaExpiracion()
                                .isBefore(LocalDateTime.now())) {

                        // Eliminamos el token vencido para que no pueda
                        // utilizarse nuevamente.
                        emailVerificationTokenRepository.delete(
                                        verificationToken);

                        throw new IllegalArgumentException(
                                        "El token de confirmación ha expirado.");
                }

                // Obtenemos el usuario asociado al token.
                Usuario usuario = verificationToken.getUsuario();

                // Habilitamos la cuenta.
                usuario.setEnabled(true);

                // Guardamos el usuario actualizado.
                usuarioRepository.save(usuario);

                // El token ya cumplió su función.
                //
                // Lo eliminamos para impedir que pueda reutilizarse.
                emailVerificationTokenRepository.delete(
                                verificationToken);
        }
}