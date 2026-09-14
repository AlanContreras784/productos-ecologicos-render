package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techlab.productos_ecologicos.dto.ApiResponse;
import com.techlab.productos_ecologicos.dto.CategoriaDTO;
import com.techlab.productos_ecologicos.dto.CategoriaRequestDTO;
import com.techlab.productos_ecologicos.services.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
@Tag(
    name = "Categorías",
    description = "Operaciones relacionadas con la gestión de categorías."
)
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) {
        this.service = service;
    }
    // Obtiene todas las categorías disponibles.
    @Operation(
        summary = "Listar categorías",
        description = "Devuelve todas las categorías registradas."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Categorías obtenidas correctamente"
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaDTO>>> listarCategorias() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categorías obtenidas correctamente.",
                        service.obtenerCategoriasResponse()
                )
        );
    }
    // Obtiene una categoría por su id.
    @Operation(
        summary = "Obtener categoría",
        description = "Devuelve una categoría mediante su identificador."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Categoría encontrada"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "Categoría no encontrada"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> obtenerCategoria(
            @PathVariable("id") Integer id) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categoría obtenida correctamente.",
                        service.obtenerCategoriaResponse(id)
                )
        );
    }
    // Crea una nueva categoría.
    @Operation(
        summary = "Crear categoría",
        description = "Registra una nueva categoría."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "Categoría creada correctamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "Datos inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaDTO>> crearCategoria(
            @Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Categoría creada correctamente.",
                                service.crearCategoria(dto)
                        )
                );
    }
    // Actualiza una categoría existente.
    @Operation(
        summary = "Actualizar categoría",
        description = "Modifica una categoría existente."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Categoría actualizada correctamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "Categoría no encontrada"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> actualizarCategoria(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categoría actualizada correctamente.",
                        service.actualizarCategoria(id, dto)
                )
        );
    }
    // Elimina una categoría.
    @Operation(
        summary = "Eliminar categoría",
        description = "Elimina una categoría del sistema."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Categoría eliminada correctamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "Categoría no encontrada"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCategoria(
            @PathVariable("id") Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categoría eliminada correctamente.",
                        null
                )
        );
    }
}