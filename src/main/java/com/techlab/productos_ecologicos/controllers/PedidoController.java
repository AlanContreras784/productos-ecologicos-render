
package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.techlab.productos_ecologicos.dto.ApiResponse;
import com.techlab.productos_ecologicos.dto.PedidoRequestDTO;
import com.techlab.productos_ecologicos.dto.PedidoResponseDTO;
import com.techlab.productos_ecologicos.services.PedidoService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pedidos")
@Tag(
    name = "Pedidos",
    description = "Operaciones relacionadas con los pedidos de compra."
)
public class PedidoController {

    private final PedidoService pedidoService;

    // Inicializa el servicio que gestiona los pedidos.
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Crea un pedido pendiente a partir del carrito activo del usuario.
    @Operation(
        summary = "Crear pedido",
        description = "Crea un pedido pendiente utilizando el carrito activo del usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Pedido creado correctamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o carrito vacío"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Usuario no autenticado"
        )
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> crearPedido(
            @Valid @RequestBody PedidoRequestDTO request) {

        PedidoResponseDTO pedido = pedidoService.crearPedido(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                    true,
                    "Pedido creado correctamente. Pendiente de pago.",
                    pedido
                ));
    }

    // Obtiene los pedidos del usuario autenticado.
    @Operation(
        summary = "Listar mis pedidos",
        description = "Devuelve únicamente los pedidos del usuario autenticado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Pedidos obtenidos correctamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Usuario no autenticado"
        )
    })
    @GetMapping("/mis-pedidos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<PedidoResponseDTO>>> listarMisPedidos() {

        List<PedidoResponseDTO> pedidos = pedidoService.listarMisPedidos();

        return ResponseEntity.ok(
                new ApiResponse<>(
                    true,
                    "Pedidos obtenidos correctamente.",
                    pedidos
                )
        );
    }

    
    // Cancela un pedido pendiente y devuelve las unidades reservadas al stock.
    @Operation(
        summary = "Cancelar pedido",
        description = "Cancela un pedido pendiente del usuario autenticado y devuelve el stock reservado."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Pedido cancelado correctamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Usuario no autenticado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "El pedido no pertenece al usuario autenticado"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Pedido inexistente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "El pedido no puede cancelarse"
        )
    })
    @PatchMapping("/{pedidoId}/cancelar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> cancelarPedido(
            @PathVariable Integer pedidoId) {

        pedidoService.cancelarPedido(pedidoId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                    true,
                    "Pedido cancelado correctamente. El stock fue devuelto.",
                    null
                )
        );
    }

}
