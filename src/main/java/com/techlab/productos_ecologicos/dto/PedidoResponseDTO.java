package com.techlab.productos_ecologicos.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para devolver la información completa
 * de un pedido al frontend.
 *
 * Incluye:
 * - Identificación del pedido.
 * - Fecha.
 * - Estado.
 * - Datos de envío.
 * - Importes.
 * - Productos comprados.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información completa de un pedido.")
public class PedidoResponseDTO {

    @Schema(description = "Identificador del pedido", example = "15")
    private Integer id;

    @Schema(description = "Fecha y hora de creación del pedido")
    private LocalDateTime fecha;

    @Schema(description = "Estado actual del pedido", example = "PENDIENTE")
    private String estado;

    // Datos del comprador y envío.

    @Schema(description = "Nombre del comprador", example = "Alan")
    private String nombre;

    @Schema(description = "Apellido del comprador", example = "Contreras")
    private String apellido;

    @Schema(description = "Email del comprador", example = "alan@email.com")
    private String email;

    @Schema(description = "Teléfono de contacto", example = "1123456789")
    private String telefono;

    @Schema(description = "Dirección de entrega", example = "Av. Siempre Viva 123")
    private String direccion;

    @Schema(description = "Ciudad de entrega", example = "Quilmes")
    private String ciudad;

    @Schema(description = "Provincia de entrega", example = "Buenos Aires")
    private String provincia;

    @Schema(description = "Código postal de entrega", example = "1878")
    private String codigoPostal;

    // Importes del pedido.

    @Schema(description = "Subtotal del pedido", example = "5000.00")
    private Double subtotal;

    @Schema(description = "Costo de envío", example = "0.00")
    private Double envio;

    @Schema(description = "Total del pedido", example = "5000.00")
    private Double total;

    // Productos comprados.

    @Schema(description = "Productos incluidos en el pedido")
    private List<PedidoProductoResponseDTO> productos;
}