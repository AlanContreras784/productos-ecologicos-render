package com.techlab.productos_ecologicos.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado del envío de correos electrónicos.
 *
 * Responsabilidades:
 *
 * - Enviar el correo de confirmación de cuenta.
 * - Mantener separada la comunicación por email
 * de la lógica de autenticación.
 */
@Service
@Slf4j
public class EmailService {

        @Value("${spring.mail.username}")
        private String emailAdministrador;

        private final JavaMailSender mailSender;

        /**
         * Inyección del componente encargado de enviar emails.
         *
         * @param mailSender componente proporcionado por Spring Mail.
         */
        public EmailService(JavaMailSender mailSender) {
                this.mailSender = mailSender;
        }

        /**
         * Envía un correo de confirmación al usuario.
         *
         * El token se incluye dentro de un enlace que el usuario
         * utilizará posteriormente para confirmar su cuenta.
         *
         * @param email dirección de correo del usuario.
         * @param token token generado para confirmar la cuenta.
         */
        public void enviarEmailConfirmacion(
                        String email,
                        String token) {
                log.info(">>> EMAIL SERVICE EJECUTADO. Destinatario: {}", email);
                String enlaceConfirmacion = "http://localhost:8080/auth/confirmar-email?token="
                                + token;

                SimpleMailMessage mensaje = new SimpleMailMessage();

                mensaje.setTo(email);
                mensaje.setSubject(
                                "Cero Huella - Confirmación de cuenta");

                mensaje.setText(
                                "Hola,\n\n"
                                                + "Gracias por registrarte en Cero Huella.\n\n"
                                                + "Para confirmar tu cuenta, ingresá al siguiente enlace:\n\n"
                                                + enlaceConfirmacion
                                                + "\n\n"
                                                + "Si no realizaste este registro, podés ignorar este correo.\n\n"
                                                + "Cero Huella");
                try {

                        log.info(">>> INTENTANDO ENVIAR EMAIL...");

                        mailSender.send(mensaje);

                        log.info(">>> EMAIL ENVIADO CORRECTAMENTE A: {}", email);

                } catch (Exception e) {

                        log.error(">>> ERROR AL ENVIAR EMAIL", e);

                        throw e;
                }
        }

        /**
         * Envía una notificación al correo de Cero Huella
         * informando que se registró un nuevo usuario.
         *
         * El correo administrativo utiliza la misma cuenta
         * configurada como remitente en spring.mail.username.
         *
         * @param username username del nuevo usuario.
         * @param email    email del nuevo usuario.
         * @param nombre   nombre del nuevo usuario.
         * @param apellido apellido del nuevo usuario.
         */
        public void enviarNotificacionNuevoRegistro(
                        String username,
                        String email,
                        String nombre,
                        String apellido) {

                SimpleMailMessage mensaje = new SimpleMailMessage();

                mensaje.setTo(emailAdministrador);

                mensaje.setSubject(
                                "Cero Huella - Nuevo usuario registrado");

                mensaje.setText(
                                "Se registró un nuevo usuario en Cero Huella.\n\n"
                                                + "Datos del usuario:\n\n"
                                                + "Username: " + username + "\n"
                                                + "Email: " + email + "\n"
                                                + "Nombre: " + nombre + "\n"
                                                + "Apellido: " + apellido + "\n"
                                                + "Fecha de registro: " + LocalDateTime.now() + "\n\n"
                                                + "Cero Huella");

                try {

                        log.info(
                                        ">>> ENVIANDO NOTIFICACIÓN DE NUEVO REGISTRO A: {}",
                                        emailAdministrador);

                        mailSender.send(mensaje);

                        log.info(
                                        ">>> NOTIFICACIÓN DE NUEVO REGISTRO ENVIADA CORRECTAMENTE");

                } catch (Exception e) {

                        log.error(
                                        ">>> ERROR AL ENVIAR NOTIFICACIÓN DE NUEVO REGISTRO",
                                        e);

                        throw e;
                }
        }
}