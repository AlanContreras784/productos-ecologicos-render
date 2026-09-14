package com.techlab.productos_ecologicos.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.techlab.productos_ecologicos.dto.UsuarioDTO;
import com.techlab.productos_ecologicos.models.Usuario;

/**

* Mapper encargado de convertir entidades Usuario
* en objetos UsuarioDTO.
*
* Responsabilidades:
* * Convertir un Usuario individual a UsuarioDTO.
* * Convertir listas de Usuario a listas de UsuarioDTO.
* * Evitar exponer información sensible como la contraseña.
*
* El campo role se convierte desde el enum Role
* a String utilizando el método name().
  */
  @Component
  public class UsuarioMapper {

  /**

  * Convierte una entidad Usuario en un UsuarioDTO.
  *
  * @param usuario entidad obtenida desde la base de datos.
  * @return DTO con la información pública del usuario.
    */
    public UsuarioDTO toDTO(Usuario usuario) {

    if (usuario == null) {
    return null;
    }

    UsuarioDTO dto = new UsuarioDTO();

    dto.setId(
    usuario.getId()
    );

    dto.setUsername(
    usuario.getUsername()
    );

    dto.setNombre(
    usuario.getNombre()
    );

    dto.setApellido(
    usuario.getApellido()
    );

    /*

    * Convierte el enum Role a String.
    *
    * Ejemplos:
    * Role.ADMIN -> "ADMIN"
    * Role.USER  -> "USER"
      */
      if (usuario.getRole() != null) {

      dto.setRole(
      usuario.getRole().name()
      );
      }

    return dto;
    }

  /**

  * Convierte una lista de entidades Usuario
  * en una lista de UsuarioDTO.
  *
  * @param usuarios lista de usuarios obtenida desde la base de datos.
  * @return lista de DTOs con información pública de los usuarios.
    */
    public List<UsuarioDTO> toDTOList(
    List<Usuario> usuarios
    ) {

    return usuarios
    .stream()
    .map(this::toDTO)
    .toList();
    }
    }

