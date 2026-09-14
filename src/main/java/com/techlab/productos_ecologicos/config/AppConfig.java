package com.techlab.productos_ecologicos.config;

import com.techlab.productos_ecologicos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UsuarioRepository usuarioRepository;

    // Cómo cargar un usuario desde la base de datos dado su username.
    // Spring Security llama a esto internamente cuando verifica credenciales.
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No se encontró un usuario con username: " + username));
    }

    // BCrypt para encriptar y comparar contraseñas.
    // Es una función de hash unidireccional: nunca desencripta.
    // Al hacer login, BCrypt vuelve a aplicar el hash a la contraseña ingresada
    // y compara el resultado con el hash guardado en la base de datos.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Combina UserDetailsService y PasswordEncoder.
    //cambio con el codigo anterior porque en el anterior Sping Boot no estaba inyectando el UserDetailsService y PasswordEncoder correctamente, lo que causaba errores de autenticación. Ahora se asegura de que ambos estén configurados en el DaoAuthenticationProvider. y la Api de Spring cambio de constructor vacio a uno que recibe el UserDetailsService y PasswordEncoder como parámetros, lo que hace que la configuración sea más explícita y menos propensa a errores. 
    // Configura el AuthenticationProvider utilizando el UserDetailsService
    // y el PasswordEncoder definidos en la aplicación.
    //
    // Esta implementación es compatible con la configuración de
    // Spring Security 6 / Spring Boot 4.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    // El orquestador de autenticación — delega en el AuthenticationProvider.
    // Es el que llamamos desde AuthService.login() para verificar las credenciales.
    // Expone el AuthenticationManager como Bean para poder inyectarlo
    // en AuthService y delegar la validación de credenciales.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
