package com.techlab.productos_ecologicos.services;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado del envío de correos electrónicos mediante Resend.
 *
 * Responsabilidades:
 *
 * - Enviar el correo de confirmación de cuenta.
 * - Enviar una notificación cuando se registra un nuevo usuario.
 * - Mantener separada la comunicación por email
 *   de la lógica de autenticación.
 */
@Service
@Slf4j
public class EmailService {

    /**
     * URL base del backend.
     *
     * En local utiliza http://localhost:8080.
     * En producción se configura mediante APP_BASE_URL.
     */
    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Correo administrativo de Cero Huella.
     *
     * Se utiliza para recibir las notificaciones
     * de nuevos registros.
     */
    @Value("${MAIL_USERNAME}")
    private String emailAdministrador;

    private final Resend resend;

    /**
     * Inicializa el cliente de Resend utilizando
     * la API Key configurada en las variables de entorno.
     *
     * @param resendApiKey API Key de Resend.
     */
    public EmailService(
            @Value("${RESEND_API_KEY}") String resendApiKey) {

        this.resend = new Resend(resendApiKey);
    }

    /**
     * Envía un correo de confirmación al usuario.
     *
     * El token se incluye dentro de un enlace que el usuario
     * utilizará posteriormente para confirmar su cuenta.
     *
     * El remitente temporal es onboarding@resend.dev.
     *
     * @param email dirección de correo del usuario.
     * @param token token generado para confirmar la cuenta.
     */
    public void enviarEmailConfirmacion(
            String email,
            String token) {

        log.info(
                ">>> EMAIL SERVICE RESEND EJECUTADO. Destinatario: {}",
                email);

        String enlaceConfirmacion =
                baseUrl
                        + "/auth/confirmar-email?token="
                        + token;

        String cuerpoEmail =
                "Hola,\n\n"
                        + "Gracias por registrarte en Cero Huella.\n\n"
                        + "Para confirmar tu cuenta, ingresá al siguiente enlace:\n\n"
                        + enlaceConfirmacion
                        + "\n\n"
                        + "Si no realizaste este registro, podés ignorar este correo.\n\n"
                        + "Cero Huella";

        try {

            log.info(">>> INTENTANDO ENVIAR EMAIL MEDIANTE RESEND...");

            CreateEmailOptions params =
                    CreateEmailOptions.builder()
                            .from("onboarding@resend.dev")
                            .to(email)
                            .subject(
                                    "Cero Huella - Confirmación de cuenta")
                            .text(cuerpoEmail)
                            .build();

            CreateEmailResponse response =
                    resend.emails().send(params);

            log.info(
                    ">>> EMAIL ENVIADO CORRECTAMENTE MEDIANTE RESEND. ID: {}",
                    response.getId());

        } catch (ResendException e) {

            log.error(
                    ">>> ERROR AL ENVIAR EMAIL MEDIANTE RESEND",
                    e);

            throw new RuntimeException(
                    "No se pudo enviar el correo de confirmación.",
                    e);
        }
    }

    /**
     * Envía una notificación al correo de Cero Huella
     * informando que se registró un nuevo usuario.
     *
     * El correo administrativo se obtiene de MAIL_USERNAME.
     *
     * @param username username del nuevo usuario.
     * @param email email del nuevo usuario.
     * @param nombre nombre del nuevo usuario.
     * @param apellido apellido del nuevo usuario.
     */
    public void enviarNotificacionNuevoRegistro(
            String username,
            String email,
            String nombre,
            String apellido) {

        String cuerpoEmail =
                "Se registró un nuevo usuario en Cero Huella.\n\n"
                        + "Datos del usuario:\n\n"
                        + "Username: " + username + "\n"
                        + "Email: " + email + "\n"
                        + "Nombre: " + nombre + "\n"
                        + "Apellido: " + apellido + "\n"
                        + "Fecha de registro: "
                        + LocalDateTime.now()
                        + "\n\n"
                        + "Cero Huella";

        try {

            log.info(
                    ">>> ENVIANDO NOTIFICACIÓN DE NUEVO REGISTRO MEDIANTE RESEND A: {}",
                    emailAdministrador);

            CreateEmailOptions params =
                    CreateEmailOptions.builder()
                            .from("onboarding@resend.dev")
                            .to("alancontreras784@gmail.com")
                            .subject(
                                    "Cero Huella - Nuevo usuario registrado")
                            .text(cuerpoEmail)
                            .build();

            CreateEmailResponse response =
                    resend.emails().send(params);

            log.info(
                    ">>> NOTIFICACIÓN ENVIADA CORRECTAMENTE MEDIANTE RESEND. ID: {}",
                    response.getId());

        } catch (ResendException e) {

            log.error(
                    ">>> ERROR AL ENVIAR NOTIFICACIÓN MEDIANTE RESEND",
                    e);

            throw new RuntimeException(
                    "No se pudo enviar la notificación de nuevo registro.",
                    e);
        }
    }
}