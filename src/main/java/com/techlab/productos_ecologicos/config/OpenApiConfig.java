package com.techlab.productos_ecologicos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
    // Configura la documentación OpenAPI de la aplicación.
    @Bean
    public OpenAPI productosEcologicosAPI() {
        final String securitySchemeName = "Bearer Authentication";
        return new OpenAPI()
                // Información general de la API.
                .info(
                        new Info()
                                .title("API Productos Ecológicos")
                                .version("1.0.0")
                                .description("""
                                        API REST desarrollada con Spring Boot.

                                        Funcionalidades:

                                        • Autenticación JWT
                                        • CRUD Productos
                                        • CRUD Categorías
                                        • Carrito de compras
                                        • Manejo automático de stock
                                        • DTOs
                                        • ApiResponse
                                        • ApiError
                                        """)
                                .contact(
                                        new Contact()
                                                .name("Alan Contreras")
                                                .url("https://github.com/AlanContreras784")
                                )
                )
                // Indica que la API utiliza autenticación JWT.
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )
                // Configuración del esquema Bearer Token.
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
