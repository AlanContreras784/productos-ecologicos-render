package com.techlab.productos_ecologicos.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techlab.productos_ecologicos.dto.ApiResponse;
import com.techlab.productos_ecologicos.dto.LoginRequestDTO;
import com.techlab.productos_ecologicos.dto.LoginResponseDTO;
import com.techlab.productos_ecologicos.dto.RegistroRequestDTO;
import com.techlab.productos_ecologicos.dto.RegistroResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller encargado de gestionar la autenticación.
 *
 * Responsabilidades:
 *
 * - Recibir solicitudes de login.
 * - Recibir solicitudes de registro.
 * - Delegar la lógica al AuthService.
 * - Devolver respuestas estandarizadas mediante ApiResponse.
 *
 * No contiene lógica de seguridad.
 * Esa responsabilidad pertenece a Spring Security y AuthService.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones relacionadas con registro e inicio de sesión mediante JWT.")
public class AuthController {
        private final AuthService authService;

        /**
         * Permite autenticar un usuario existente.
         *
         * Recibe:
         * - Username
         * - Password
         *
         * Devuelve:
         * - Token JWT
         * - Usuario autenticado
         * - Rol
         */
        @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
        })
        @PostMapping("/login")
        public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
                        @Valid @RequestBody LoginRequestDTO request) {
                LoginResponseDTO response = authService.login(request);
                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Inicio de sesión exitoso.",
                                                response));
        }

        /**
         * Registra un nuevo usuario.
         *
         * Flujo:
         *
         * 1. Recibe datos del formulario.
         * 2. AuthService crea el usuario.
         * 3. Se genera JWT automáticamente.
         * 4. Devuelve la sesión iniciada.
         */
        @Operation(summary = "Registrar usuario", description = "Crea un usuario nuevo y devuelve un token JWT.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos")
        })
        @PostMapping("/register")
        public ResponseEntity<ApiResponse<RegistroResponseDTO>> register(
                        @Valid @RequestBody RegistroRequestDTO request) {
                RegistroResponseDTO response = authService.register(request);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                new ApiResponse<>(
                                                                true,
                                                                "Usuario registrado correctamente. Revisa tu correo para confirmar la cuenta.",
                                                                response));
        }

        /**
         * Confirma la dirección de email de un usuario.
         *
         * Recibe el token enviado mediante el correo
         * de confirmación.
         *
         * Una vez validado el token, la cuenta queda habilitada
         * y el usuario puede iniciar sesión.
         */
        @Operation(summary = "Confirmar email", description = "Confirma la dirección de email mediante el token recibido.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Email confirmado correctamente"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Token inválido o expirado")
        })
        @GetMapping("/confirmar-email")
        public ResponseEntity<ApiResponse<Void>> confirmarEmail(
                        @RequestParam String token) {

                authService.confirmarEmail(token);

                return ResponseEntity.ok(
                                new ApiResponse<>(
                                                true,
                                                "Email confirmado correctamente. Ya puedes iniciar sesión.",
                                                null));
        }
}