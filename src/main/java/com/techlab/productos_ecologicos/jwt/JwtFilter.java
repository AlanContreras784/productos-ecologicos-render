package com.techlab.productos_ecologicos.jwt;

import com.techlab.productos_ecologicos.models.Usuario;
import com.techlab.productos_ecologicos.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {

            // ======================================================
            // 1. Extraer el token del header Authorization
            // ======================================================

            final String token = extraerTokenDelRequest(request);

            // ======================================================
            // 2. Si no hay token, continuar normalmente.
            //
            // Los endpoints públicos podrán continuar.
            // Los endpoints protegidos serán bloqueados
            // posteriormente por Spring Security.
            // ======================================================

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // ======================================================
            // 3. Extraer username del JWT
            // ======================================================

            final String username = jwtService.obtenerUsername(token);

            // ======================================================
            // 4. Si existe username y todavía no hay autenticación,
            // buscamos al usuario.
            // ======================================================

            if (username != null
                    && SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                Usuario usuario = usuarioRepository
                        .findByUsername(username)
                        .orElse(null);

                // ==================================================
                // 5. Validar token
                // ==================================================

                if (usuario != null
                        && jwtService.esTokenValido(token, usuario)) {

                    // ==============================================
                    // 6. Crear autenticación
                    // ==============================================

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            usuario.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    // ==============================================
                    // 7. Registrar usuario autenticado
                    // ==============================================

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }
            }

        } catch (Exception e) {

            // ======================================================
            // El JWT es inválido o está alterado.
            //
            // No dejamos continuar una autenticación inválida.
            // ======================================================

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");

            response.getWriter().write("""
                    {
                        "error": "UNAUTHORIZED",
                        "message": "Token inválido o alterado"
                    }
                    """);

            return;
        }

        // ==========================================================
        // MUY IMPORTANTE:
        //
        // El filterChain queda FUERA del try/catch.
        //
        // De esta manera, una excepción posterior no puede ser
        // convertida accidentalmente en un 401 por este filtro.
        // ==========================================================

        filterChain.doFilter(request, response);
    }

    // Extrae el token del header "Authorization: Bearer <token>"
    // Corta los primeros 7 caracteres ("Bearer ") para quedarse solo con el token
    private String extraerTokenDelRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
