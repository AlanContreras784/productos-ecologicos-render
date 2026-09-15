package com.techlab.productos_ecologicos.controller;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.productos_ecologicos.dto.ApiResponse;

/**
 * Controller TEMPORAL utilizado exclusivamente para diagnosticar
 * la conectividad SMTP desde el entorno de producción.
 *
 * IMPORTANTE:
 *
 * Este controller NO envía correos electrónicos.
 *
 * Su único objetivo es comprobar si el servidor donde está
 * ejecutándose la aplicación puede establecer una conexión TCP
 * con smtp.gmail.com en el puerto 587.
 *
 * Una vez finalizado el diagnóstico del problema SMTP en Render,
 * ESTE CONTROLLER DEBE SER ELIMINADO.
 */
@RestController
@RequestMapping("/diagnostico")
public class DiagnosticoController {

    /**
     * Comprueba si el servidor puede conectarse a Gmail SMTP.
     *
     * Esta prueba intenta establecer únicamente una conexión TCP.
     * No utiliza JavaMailSender y no realiza autenticación ni
     * envío de correos.
     *
     * Endpoint temporal:
     *
     * GET /diagnostico/smtp
     */
    @GetMapping("/smtp")
    public ResponseEntity<ApiResponse<String>> diagnosticarSmtp() {

        String host = "smtp.gmail.com";
        int port = 587;

        try (Socket socket = new Socket()) {

            socket.connect(
                    new InetSocketAddress(host, port),
                    10000);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Conexión TCP con Gmail SMTP establecida correctamente.",
                            "Host: " + host + ":" + port));

        } catch (Exception e) {

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            false,
                            "No se pudo establecer conexión TCP con Gmail SMTP.",
                            e.getClass().getSimpleName()
                                    + ": "
                                    + e.getMessage()));
        }
    }
}