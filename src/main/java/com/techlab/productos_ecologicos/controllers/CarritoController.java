package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techlab.productos_ecologicos.dto.ApiResponse;
import com.techlab.productos_ecologicos.dto.CarritoResponseDTO;
import com.techlab.productos_ecologicos.dto.CarritoResumenDTO;
import com.techlab.productos_ecologicos.models.Carrito;
import com.techlab.productos_ecologicos.services.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carritos")
@Tag(
    name = "Carritos",
    description = "Operaciones relacionadas con el carrito de compras."
)
public class CarritoController {
        private final CarritoService service;
        public CarritoController(CarritoService service) {
                this.service = service;
        }
        // Obtiene todos los carritos del sistema.
        // Devuelve DTOs para no exponer las entidades JPA.
        @Operation(
                summary = "Listar carritos",
                description = "Devuelve todos los carritos registrados en el sistema."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "Carritos obtenidos correctamente"
                )
        })
        @GetMapping
        public ResponseEntity<ApiResponse<List<CarritoResponseDTO>>> listarTodos() {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Carritos obtenidos correctamente.",
                                service.listarTodosDTO()
                        )
                );
        }
        // Devuelve el carrito activo del usuario autenticado.
        @Operation(
                summary = "Obtener mi carrito",
                description = "Devuelve el carrito activo del usuario autenticado."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "Carrito obtenido correctamente"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "401",
                        description = "Usuario no autenticado"
                )
        })
        @GetMapping("/mi-carrito")
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> obtenerMiCarrito() {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Carrito obtenido correctamente.",
                                service.obtenerMiCarritoDTO()
                        )
                );
        }
        // Devuelve el resumen del carrito.
        @Operation(
                summary = "Resumen del carrito",
                description = "Devuelve subtotal, envío, total y cantidad de productos."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "Resumen obtenido correctamente"
                )
        })
        @GetMapping("/mi-carrito/resumen")
        public ResponseEntity<ApiResponse<CarritoResumenDTO>> obtenerMiResumen() {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Resumen del carrito obtenido correctamente.",
                                service.obtenerResumen()
                        )
                );
        }
        // Crea un carrito vacío.
        @Operation(
                summary = "Crear carrito",
                description = "Crea un carrito nuevo asociado al usuario autenticado."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "Carrito creado correctamente"
                )
        })
        @PostMapping
        public ResponseEntity<ApiResponse<Carrito>> crear() {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(
                                new ApiResponse<>(
                                        true,
                                        "Carrito creado correctamente.",
                                        service.crear()
                                )
                        );
        }
        // Agrega un producto al carrito.
        @Operation(
                summary = "Agregar producto",
                description = "Agrega una unidad de un producto al carrito."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "Producto agregado correctamente"
                )
        })
        @PostMapping("/productos/{productoId}")
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> agregarProducto(
                @PathVariable("productoId") Integer productoId) {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Producto agregado al carrito correctamente.",
                                service.agregarProductoDTO(productoId)
                        )
                );
        }
        // Descuenta una unidad de un producto.
        @Operation(
                summary = "Descontar producto",
                description = "Descuenta una unidad del producto del carrito."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "Producto descontado correctamente"
                )
        })
        @PutMapping("/productos/{productoId}/descontar")
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> descontarProducto(
                @PathVariable("productoId") Integer productoId) {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Producto descontado correctamente.",
                                service.descontarProductoDTO(productoId)
                        )
                );
        }
        // Elimina completamente un producto del carrito.
        @Operation(
                summary = "Eliminar producto del carrito",
                description = "Elimina todas las unidades de un producto del carrito."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "Producto eliminado correctamente"
                )
        })
        @DeleteMapping("/productos/{productoId}")
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> eliminarProducto(
                @PathVariable("productoId") Integer productoId) {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Producto eliminado del carrito correctamente.",
                                service.eliminarProductoDTO(productoId)
                        )
                );
        }
        // Vacía el carrito.
        @Operation(
                summary = "Vaciar carrito",
                description = "Elimina todos los productos del carrito."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "Carrito vaciado correctamente"
                )
        })
        @DeleteMapping("/mi-carrito/vaciar")
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> vaciar() {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Carrito vaciado correctamente.",
                                service.vaciarDTO()
                        )
                );
        }
        // Elimina un carrito.
        @Operation(
                summary = "Eliminar carrito",
                description = "Elimina completamente un carrito."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "Carrito eliminado correctamente"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "Carrito no encontrado"
                )
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> eliminar(
                @PathVariable("id") Integer id) {
                service.eliminar(id);
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Carrito eliminado correctamente.",
                                null
                        )
                );
        }
}