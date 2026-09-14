package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.productos_ecologicos.dto.ApiResponse;
import com.techlab.productos_ecologicos.dto.UsuarioDTO;
import com.techlab.productos_ecologicos.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 
 * Controller encargado de exponer los endpoints
 * relacionados con los usuarios.
 *
 * Por el momento permite:
 * * Listar los usuarios registrados.
 * * Obtener la cantidad total de usuarios.
 *
 * Estos endpoints serán utilizados principalmente
 * por el Panel de Administración.
 */
@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la consulta de usuarios.")
public class UsuarioController {

  private final UsuarioService usuarioService;

  /**
   * 
   * Inyección del servicio de usuarios.
   *
   * @param usuarioService servicio encargado de la lógica
   *                       ```
   *                       relacionada con usuarios.
   *                       ```
   * 
   */
  public UsuarioController(
      UsuarioService usuarioService) {

    this.usuarioService = usuarioService;

  }

  /**
   * 
   * Obtiene todos los usuarios registrados.
   *
   * La información se devuelve mediante UsuarioDTO
   * para evitar exponer datos sensibles como la contraseña.
   *
   * Endpoint:
   * GET /usuarios
   *
   * @return lista de usuarios registrados.
   */
  @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios registrados en el sistema.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente")
  })
  @GetMapping
  public ResponseEntity<ApiResponse<List<UsuarioDTO>>> listarUsuarios() {

    return ResponseEntity.ok(
        new ApiResponse<>(
            true,
            "Usuarios obtenidos correctamente.",
            usuarioService.obtenerUsuarios()));
  }

  /**
   * 
   * Obtiene la cantidad total de usuarios registrados.
   *
   * Este endpoint se utilizará para mostrar el
   * contador de usuarios en el Dashboard.
   *
   * Endpoint:
   * GET /usuarios/count
   *
   * @return cantidad total de usuarios.
   */
  @Operation(summary = "Contar usuarios", description = "Devuelve la cantidad total de usuarios registrados.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cantidad de usuarios obtenida correctamente")
  })
  @GetMapping("/count")
  public ResponseEntity<ApiResponse<Long>> contarUsuarios() {

    return ResponseEntity.ok(
        new ApiResponse<>(
            true,
            "Cantidad de usuarios obtenida correctamente.",
            usuarioService.contarUsuarios()));
  }
}
