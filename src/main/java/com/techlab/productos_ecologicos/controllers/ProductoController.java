package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techlab.productos_ecologicos.dto.ApiResponse;
import com.techlab.productos_ecologicos.dto.ProductoRequestDTO;
import com.techlab.productos_ecologicos.dto.ProductoResponseDTO;
import com.techlab.productos_ecologicos.services.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/productos")
@Tag(
    name = "Productos",
    description = "Operaciones relacionadas con la gestión de productos ecológicos."
)
public class ProductoController {
        private final ProductoService service;
        public ProductoController(ProductoService service) {
                this.service = service;
        }

        // Obtiene todos los productos y devuelve solamente los datos necesarios para el frontend.
        @Operation(
                summary = "Listar productos",
                description = "Devuelve la lista completa de productos disponibles."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Productos obtenidos correctamente"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor"
                )
        })
        @GetMapping
        public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> listarProductos() {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Productos obtenidos correctamente.",
                                service.obtenerProductosResponse()
                        )
                );
        }

        // Obtiene solamente los productos marcados como destacados.
        // Devuelve DTOs para evitar exponer directamente la entidad JPA.
        @Operation(
                summary = "Listar productos destacados",
                description = "Devuelve todos los productos marcados como destacados."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "Productos destacados obtenidos correctamente"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor"
                )
        })
        @GetMapping("/destacados")
        public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> listarProductosDestacados() {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Productos destacados obtenidos correctamente.",
                                service.obtenerProductosDestacados()
                        )
                );
        }

        // Obtiene un producto por id y devuelve un DTO evitando exponer la entidad JPA.
        @Operation(
                summary = "Obtener producto",
                description = "Devuelve la información de un producto mediante su identificador."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Producto encontrado"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado"
                )
        })
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ProductoResponseDTO>> obtenerProducto(
                @PathVariable("id") Integer id) {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Producto obtenido correctamente.",
                                service.obtenerProductoResponse(id)
                        )
                );
        }

        // Busca productos por nombre y devuelve una lista de DTOs preparados para el frontend.
        @Operation(
                summary = "Buscar productos por nombre",
                description = "Devuelve todos los productos cuyo nombre contiene el texto indicado."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Búsqueda realizada correctamente"
                )
        })
        @GetMapping("/nombre/{nombre}")
        public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> buscarPorNombre(
                @PathVariable("nombre") String nombre) {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Productos encontrados.",
                                service.buscarPorNombreResponse(nombre)
                        )
                );
        }
        // Busca productos por categoría y devuelve una lista de DTOs preparados para el frontend.
        @Operation(
                summary = "Buscar productos por categoría",
                description = "Devuelve todos los productos pertenecientes a una categoría."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Productos encontrados"
                )
        })
        @GetMapping("/categoria/{categoria}")
        public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> buscarPorCategoria(
                @PathVariable("categoria") String categoria) {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Productos encontrados.",
                                service.buscarPorCategoriaResponse(categoria)
                        )
                );
        }

        // Crea un producto nuevo utilizando ProductoRequestDTO.
        @Operation(
                summary = "Crear producto",
                description = "Registra un nuevo producto en el sistema."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "Producto creado correctamente"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "Datos inválidos"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "No autenticado"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "No autorizado"
                )
        })
        @PostMapping
        public ResponseEntity<ApiResponse<ProductoResponseDTO>> crearProducto(
                @Valid @RequestBody ProductoRequestDTO dto) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(
                                new ApiResponse<>(
                                        true,
                                        "Producto creado correctamente.",
                                        service.crearProducto(dto)
                                )
                        );
        }

        // Actualiza un producto existente utilizando ProductoRequestDTO.
        @Operation(
                summary = "Actualizar producto",
                description = "Modifica la información de un producto existente."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Producto actualizado correctamente"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado"
                )
        })
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<ProductoResponseDTO>> actualizarProducto(
                @PathVariable("id") Integer id,
                @Valid @RequestBody ProductoRequestDTO dto) {
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Producto actualizado correctamente.",
                                service.actualizarProducto(id, dto)
                        )
                );
        }

        // Elimina un producto por id.
        @Operation(
                summary = "Eliminar producto",
                description = "Elimina un producto del sistema."
        )
        @ApiResponses({
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Producto eliminado correctamente"
                ),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado"
                )
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> eliminarProducto(
                @PathVariable("id") Integer id) {
                service.eliminar(id);
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Producto eliminado correctamente.",
                                null
                        )
                );
        }
}