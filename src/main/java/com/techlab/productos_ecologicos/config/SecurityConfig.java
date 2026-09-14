package com.techlab.productos_ecologicos.config;

import com.techlab.productos_ecologicos.auth.CustomOAuth2UserService;
import com.techlab.productos_ecologicos.auth.OAuth2AuthenticationSuccessHandler;
import com.techlab.productos_ecologicos.jwt.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authProvider;

    // Servicio personalizado encargado de procesar
    // los usuarios que llegan desde Google mediante OAuth2.
    private final CustomOAuth2UserService customOAuth2UserService;

    // Handler que genera nuestro JWT después
    // de una autenticación exitosa con Google.
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        return http

                // ======================================================
                // CSRF
                // ======================================================
                //
                // No utilizamos sesiones tradicionales.
                // La autenticación de nuestra API se realiza mediante JWT.
                //
                .csrf(csrf -> csrf.disable())

                // ======================================================
                // CORS
                // ======================================================
                //
                // Permite las peticiones provenientes del frontend.
                //
                .cors(cors -> cors.configurationSource(
                        corsConfigurationSource()))

                // ======================================================
                // AUTORIZACIÓN
                // ======================================================
                .authorizeHttpRequests(auth -> auth

                        // --------------------------------------------------
                        // AUTENTICACIÓN
                        // --------------------------------------------------
                        //
                        // Login, registro y confirmación de email.
                        //
                        .requestMatchers("/auth/**")
                        .permitAll()

                        // --------------------------------------------------
                        // OAUTH2 GOOGLE
                        // --------------------------------------------------
                        //
                        // Permite iniciar el flujo OAuth2 sin JWT.
                        //
                        .requestMatchers("/oauth2/**")
                        .permitAll()

                        .requestMatchers("/login/**")
                        .permitAll()

                        // --------------------------------------------------
                        // CORS PREFLIGHT
                        // --------------------------------------------------
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        // --------------------------------------------------
                        // SWAGGER
                        // --------------------------------------------------
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()

                        // ==================================================
                        // PRODUCTOS Y CATEGORÍAS
                        // ==================================================

                        // Consultas públicas.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/productos/**",
                                "/categorias/**")
                        .permitAll()

                        // ==================================================
                        // USUARIOS
                        // ==================================================

                        // Solamente ADMIN.
                        .requestMatchers("/usuarios/**")
                        .hasRole("ADMIN")

                        // ==================================================
                        // CRUD PRODUCTOS
                        // ==================================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/productos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/productos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/productos/**")
                        .hasRole("ADMIN")

                        // ==================================================
                        // RESTO
                        // ==================================================

                        // Todo lo demás requiere JWT.
                        .anyRequest()
                        .authenticated())

                // ======================================================
                // SESIONES
                // ======================================================
                //
                // La API continúa utilizando JWT para las peticiones
                // normales.
                //
                // IF_REQUIRED permite que Spring cree una sesión
                // únicamente cuando algún flujo la necesita.
                //
                // OAuth2 necesita una sesión temporal para conservar
                // la autorización mientras el usuario está en Google.
                //
                // Esto NO reemplaza nuestro sistema JWT.
                //
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.IF_REQUIRED))

                // ======================================================
                // AUTHENTICATION PROVIDER
                // ======================================================
                .authenticationProvider(authProvider)

                // ======================================================
                // OAUTH2 LOGIN
                // ======================================================
                //
                // Google autentica al usuario mediante OAuth2.
                //
                // Después:
                //
                // CustomOAuth2UserService
                // ↓
                // OAuth2AuthenticationSuccessHandler
                // ↓
                // JWT
                //
                .oauth2Login(oauth2 -> oauth2

                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(
                                oAuth2AuthenticationSuccessHandler))

                // ======================================================
                // MANEJO DE ERRORES
                // ======================================================
                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                authenticationEntryPoint())

                        .accessDeniedHandler(
                                accessDeniedHandler()))

                // ======================================================
                // JWT FILTER
                // ======================================================
                //
                // Nuestro filtro JWT continúa funcionando exactamente
                // como antes.
                //
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // ==============================================================
    // CORS
    // ==============================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(
                List.of("*"));

        config.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"));

        config.setAllowedHeaders(
                List.of("*"));

        config.setExposedHeaders(
                List.of("Authorization"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                config);

        return source;
    }

    // ==============================================================
    // ACCESS DENIED
    // ==============================================================

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {

        return (request, response, exception) -> {

            response.setStatus(
                    HttpServletResponse.SC_FORBIDDEN);

            response.setContentType(
                    "application/json");

            response.getWriter().write("""
                    {
                        "error": "FORBIDDEN",
                        "message": "No tienes permisos para realizar esta acción"
                    }
                    """);
        };
    }

    // ==============================================================
    // UNAUTHORIZED
    // ==============================================================

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {

        return (request, response, authException) -> {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType(
                    "application/json");

            response.getWriter().write("""
                    {
                        "error": "UNAUTHORIZED",
                        "message": "Debes iniciar sesión para acceder a este recurso."
                    }
                    """);
        };
    }
}