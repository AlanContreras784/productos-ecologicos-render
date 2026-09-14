package com.techlab.productos_ecologicos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.techlab.productos_ecologicos.dto.ApiError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import io.swagger.v3.oas.annotations.Hidden;


@RestControllerAdvice
@Hidden // <-- Esto le dice a Swagger: "ignora esta clase"
public class GlobalExceptionHandler {

    // Versión simplificada (solo devuelve el primer error):
    // String mensaje = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);

    // Cuando @Valid falla, Spring lanza MethodArgumentNotValidException.
    // Usamos Map porque pueden fallar varios campos a la vez:
    // { "nombre": "El nombre no puede estar vacío", "precio": "Debe ser mayor que cero" }
    
    // Se ejecuta cuando falla una validación realizada mediante @Valid.
    // Devuelve todos los errores agrupados por campo.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(
            MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        ApiError apiError = new ApiError(
                false,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                "Error de validación.",
                errores,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    // Se ejecuta cuando se solicita un producto que no existe.
    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarProductoNoEncontrado(
            ProductoNoEncontradoException ex) {
        return crearRespuestaError(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    // Se ejecuta cuando se solicita una categoría que no existe.
    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<ApiError> manejarCategoriaNoEncontrada(
            CategoriaNoEncontradaException ex) {
        return crearRespuestaError(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    // Se ejecuta cuando no hay stock suficiente para realizar la operación.
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ApiError> manejarStockInsuficiente(
            StockInsuficienteException ex) {
        return crearRespuestaError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    // Se ejecuta cuando no se encuentra un carrito.
    @ExceptionHandler(CarritoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarCarritoNoEncontrado(
            CarritoNoEncontradoException ex) {
        return crearRespuestaError(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    // Se ejecuta cuando el producto no existe dentro del carrito.
    @ExceptionHandler(ProductoNoEncontradoEnElCarritoException.class)
    public ResponseEntity<ApiError> manejarProductoNoEncontradoEnElCarrito(
            ProductoNoEncontradoEnElCarritoException ex) {
        return crearRespuestaError(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    // Se dispara cuando Hibernate intenta resolver (fetch EAGER) una relación
    // @ManyToOne cuyo id ya no existe en la base — por ejemplo, un CarritoProducto
    // que apunta a un producto que fue eliminado o recreado con otros ids.
    // Sin este handler, este caso caía en la excepción genérica y devolvía 500.
    // Se ejecuta cuando existe una referencia a una entidad que ya no está en la base de datos.
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<ApiError> manejarEntidadNoEncontrada(
            jakarta.persistence.EntityNotFoundException ex) {
        return crearRespuestaError(
                HttpStatus.NOT_FOUND,
                "El carrito contiene una referencia a un producto que ya no existe. Vacíe el carrito o elimínelo y cree uno nuevo."
        );
    }

    // Se ejecuta cuando el usuario o la contraseña son incorrectos.
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> manejarCredencialesIncorrectas(
            BadCredentialsException ex) {
        return crearRespuestaError(
                HttpStatus.UNAUTHORIZED,
                "Usuario o contraseña incorrectos."
        );
    }

    // Se ejecuta cuando el usuario existe pero su cuenta
        // todavía está deshabilitada.
        //
        // En nuestro sistema esto significa que todavía
        // no confirmó su dirección de email.
        @ExceptionHandler(DisabledException.class)
        public ResponseEntity<ApiError> manejarCuentaNoConfirmada(
                DisabledException ex) {

        return crearRespuestaError(
                HttpStatus.UNAUTHORIZED,
                "La cuenta no está confirmada. Revisa tu correo electrónico."
        );
        }

    // Se ejecuta cuando el usuario autenticado no tiene permisos para acceder al recurso.
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiError> manejarAccesoDenegado(
            AuthorizationDeniedException ex) {
        return crearRespuestaError(
                HttpStatus.FORBIDDEN,
                "No tienes permisos para acceder a este recurso."
        );
    }

    // Captura cualquier excepción no controlada y devuelve un error interno del servidor.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarExcepcionGenerica(Exception ex) {
        return crearRespuestaError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado: " + ex.getMessage()
        );
    }

    // Crea una respuesta de error estandarizada para toda la API.
    private ResponseEntity<ApiError> crearRespuestaError(
            HttpStatus status,
            String mensaje) {
        ApiError apiError = new ApiError(
                false,
                status.value(),
                status.name(),
                mensaje,
                null,
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(status)
                .body(apiError);
    }

    
}