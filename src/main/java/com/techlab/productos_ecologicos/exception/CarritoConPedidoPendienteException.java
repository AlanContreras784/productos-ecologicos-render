
package com.techlab.productos_ecologicos.exception;

/**
 * Representa un intento de modificar o eliminar un carrito
 * que tiene un pedido pendiente.
 */
public class CarritoConPedidoPendienteException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje detalle del motivo del bloqueo.
     */
    public CarritoConPedidoPendienteException(String mensaje) {
        super(mensaje);
    }
}