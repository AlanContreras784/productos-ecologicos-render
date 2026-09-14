package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para enviar un resumen calculado del carrito.
 *
 * Se utiliza para mostrar información general del carrito
 * sin necesidad de enviar todos los productos incluidos.
 *
 * Puede ser utilizado en endpoints como:
 *
 * GET /carritos/mi-carrito/resumen
 *
 * ¿Por qué usamos un DTO?
 *
 * - Evita exponer la entidad Carrito directamente.
 * - Envía solamente información necesaria para el frontend.
 * - Permite calcular valores derivados como subtotal, envío y total.
 * - Facilita modificar la lógica de cálculo sin afectar la API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Resumen con los totales calculados de un carrito."
)
public class CarritoResumenDTO {
    // Cantidad total de productos incluidos en el carrito.
    //
    // Representa la suma de unidades agregadas,
    // no la cantidad de productos diferentes.
    @Schema(
            description = "Cantidad total de unidades de productos en el carrito",
            example = "5"
    )
    private Integer cantidadProductos;
    // Suma del precio de todos los productos antes
    // de aplicar costos adicionales.
    @Schema(
            description = "Subtotal del carrito antes del envío",
            example = "4250.50"
    )
    private Double subtotal;
    // Costo adicional correspondiente al envío.
    //
    // Puede ser calculado según reglas del negocio.
    @Schema(
            description = "Costo del envío calculado para el carrito",
            example = "500.00"
    )
    private Double envio;
    // Total final que deberá pagar el usuario.
    //
    // Normalmente corresponde a:
    //
    // subtotal + envio
    //
    @Schema(
            description = "Monto total final del carrito",
            example = "4750.50"
    )
    private Double total;
}