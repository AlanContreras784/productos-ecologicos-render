package com.techlab.productos_ecologicos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para recibir la información necesaria para crear o actualizar
 * un producto.
 *
 * Se utiliza en los endpoints:
 *
 * POST /productos
 * PUT  /productos/{id}
 *
 * ¿Por qué usamos un RequestDTO?
 *
 * - Evita exponer directamente la entidad Producto.
 * - Permite validar los datos recibidos antes de llegar al Service.
 * - El frontend solamente envía la información necesaria.
 * - Facilita mantener separada la capa de presentación y persistencia.
 * - Evita que el cliente pueda modificar relaciones JPA directamente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Datos necesarios para crear o actualizar un producto."
)
public class ProductoRequestDTO {
    // Nombre comercial del producto.
    // Es obligatorio y no puede estar vacío.
    @Schema(
            description = "Nombre del producto",
            example = "Jabón ecológico artesanal",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre;
    // Precio de venta del producto.
    // Debe existir y ser mayor a cero.
    @Schema(
            description = "Precio del producto",
            example = "850.50",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "El precio no puede estar vacío")
    @Positive(message = "El precio debe ser mayor que cero")
    private Double precio;
    // Descripción visible para el usuario.
    // Permite explicar características del producto.
    @Schema(
            description = "Descripción del producto",
            example = "Jabón biodegradable elaborado con ingredientes naturales.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
    // Cantidad disponible del producto.
    // Permite cero unidades, pero no valores negativos.
    @Schema(
            description = "Cantidad disponible en stock",
            example = "30",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;
    // URL donde se encuentra almacenada la imagen.
    // Puede provenir de Firebase Storage u otro servicio externo.
    @Schema(
            description = "URL de la imagen del producto",
            example = "https://misitio.com/imagenes/jabon.jpg",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Por favor, selecciona una imagen para continuar")
    private String imagenUrl;


    @Schema(
        description = "Indica si el producto aparece en la sección de productos destacados",
        example = "false",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "El campo destacado no puede ser nulo")
    private Boolean destacado = false;
    // Identificador de la categoría seleccionada.
    //
    // El frontend solamente envía el ID.
    // El Service será responsable de buscar la entidad Categoria
    // mediante CategoriaRepository.
    //
    // Esto evita enviar objetos completos y protege la relación JPA.
    @Schema(
            description = "Identificador de la categoría asociada al producto",
            example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "La categoría es obligatoria")
    private Integer categoriaId;
}