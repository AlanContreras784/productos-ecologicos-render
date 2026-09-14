package com.techlab.productos_ecologicos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techlab.productos_ecologicos.dto.UsuarioDTO;
import com.techlab.productos_ecologicos.mapper.UsuarioMapper;
import com.techlab.productos_ecologicos.models.Usuario;
import com.techlab.productos_ecologicos.repository.UsuarioRepository;

/**

* Servicio encargado de gestionar las operaciones
* relacionadas con los usuarios.
*
* Responsabilidades actuales:
* * Obtener todos los usuarios.
* * Convertir las entidades Usuario a UsuarioDTO.
* * Obtener la cantidad total de usuarios.
*
* Por el momento este servicio es utilizado
* principalmente por el Panel de Administración.
  */
  @Service
  public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final UsuarioMapper usuarioMapper;

  /**

  * Inyección de dependencias mediante constructor.
  *
  * @param usuarioRepository repositorio de usuarios.
  * @param usuarioMapper mapper para convertir Usuario a UsuarioDTO.
    */
    public UsuarioService(
    UsuarioRepository usuarioRepository,
    UsuarioMapper usuarioMapper
    ) {

    this.usuarioRepository =
    usuarioRepository;

    this.usuarioMapper =
    usuarioMapper;
    }

  /**

  * Obtiene todos los usuarios registrados
  * en el sistema.
  *
  * Las entidades obtenidas desde la base de datos
  * se convierten a UsuarioDTO antes de enviarlas
  * al Controller.
  *
  * Esto evita exponer información sensible,
  * como la contraseña del usuario.
  *
  * @return lista de usuarios en formato DTO.
    */
    public List<UsuarioDTO> obtenerUsuarios() {

    List<Usuario> usuarios =
    usuarioRepository.findAll();

    return usuarioMapper.toDTOList(
    usuarios
    );
    }

  /**

  * Obtiene la cantidad total de usuarios
  * registrados en el sistema.
  *
  * Se utiliza principalmente para mostrar
  * el contador dentro del Dashboard de administración.
  *
  * @return cantidad total de usuarios.
    */
    public long contarUsuarios() {

    return usuarioRepository.count();
    }
    }
