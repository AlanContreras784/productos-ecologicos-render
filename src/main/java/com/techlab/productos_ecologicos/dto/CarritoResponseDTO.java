package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO utilizado para enviar la información completa de un carrito.
 *
 * Se utiliza en las respuestas de los endpoints relacionados con el carrito.
 *
 * ¿Por qué usamos un ResponseDTO?
 *
 * - Evita exponer directamente la entidad Carrito.
 * - Permite controlar la información enviada al frontend.
 * - Evita problemas con relaciones JPA bidireccionales.
 * - Permite incluir información relacionada como usuario y productos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Información completa de un carrito perteneciente a un usuario."
)
public class CarritoResponseDTO {
    // Identificador único del carrito.
    @Schema(
            description = "Identificador del carrito",
            example = "1"
    )
    private Integer id;
    // Estado actual del carrito.
    //
    // Ejemplos:
    // ACTIVO
    // FINALIZADO
    //
    // Permite conocer la situación actual del carrito.
    @Schema(
            description = "Estado actual del carrito",
            example = "ACTIVO"
    )
    private String estado;
    // Usuario propietario del carrito.
    //
    // Se utiliza UsuarioDTO para evitar exponer
    // directamente la entidad Usuario.
    @Schema(
            description = "Usuario asociado al carrito"
    )
    private UsuarioDTO usuario;
    // Lista de productos agregados al carrito.
    //
    // Cada elemento contiene:
    // - Producto seleccionado.
    // - Cantidad agregada.
    //
    // Utiliza CarritoProductoDTO para mantener
    // una respuesta controlada hacia el frontend.
    @Schema(
            description = "Lista de productos incluidos dentro del carrito"
    )
    private List<CarritoProductoDTO> productos;
}