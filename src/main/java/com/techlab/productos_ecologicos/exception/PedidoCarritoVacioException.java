package com.techlab.productos_ecologicos.exception;

/**
 * Excepción utilizada cuando se intenta crear un pedido
 * utilizando un carrito que no contiene productos.
 */
public class PedidoCarritoVacioException extends RuntimeException {

    // Inicializa la excepción con el mensaje recibido.
    public PedidoCarritoVacioException(String mensaje) {
        super(mensaje);
    }
}