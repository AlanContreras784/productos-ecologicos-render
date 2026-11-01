package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para recibir los datos necesarios
 * para crear un pedido.
 *
 * El frontend solamente envía los datos de envío.
 *
 * El backend obtiene:
 * - El usuario autenticado.
 * - El carrito activo.
 * - Los productos.
 * - Las cantidades.
 * - Los precios.
 * - El subtotal.
 * - El envío.
 * - El total.
 * - El estado inicial del pedido.
 *
 * Esto evita confiar en información sensible enviada
 * directamente desde el navegador.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos necesarios para crear un pedido.")
public class PedidoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del comprador", example = "Alan")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del comprador", example = "Contreras")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Schema(description = "Email del comprador", example = "alan@email.com")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Schema(description = "Teléfono de contacto", example = "1123456789")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Schema(description = "Dirección de entrega", example = "Av. Siempre Viva 123")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Schema(description = "Ciudad de entrega", example = "Quilmes")
    private String ciudad;

    @NotBlank(message = "La provincia es obligatoria")
    @Schema(description = "Provincia de entrega", example = "Buenos Aires")
    private String provincia;

    @NotBlank(message = "El código postal es obligatorio")
    @Schema(description = "Código postal de entrega", example = "1878")
    private String codigoPostal;
}