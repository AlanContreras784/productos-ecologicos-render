package com.techlab.productos_ecologicos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuración para habilitar la ejecución
 * de tareas asíncronas en la aplicación.
 *
 * Permite que determinadas tareas secundarias,
 * como el envío de correos electrónicos,
 * no bloqueen la respuesta principal del backend.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}