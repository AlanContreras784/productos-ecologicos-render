
package com.techlab.productos_ecologicos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.productos_ecologicos.models.Carrito;
import com.techlab.productos_ecologicos.models.EstadoPedido;
import com.techlab.productos_ecologicos.models.Pedido;
import com.techlab.productos_ecologicos.models.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Obtiene todos los pedidos realizados por un usuario.
    List<Pedido> findByUsuario(Usuario usuario);

    // Comprueba si un carrito ya tiene un pedido asociado.
    boolean existsByCarrito(Carrito carrito);

    // Comprueba si un carrito tiene un pedido en un estado determinado.
    boolean existsByCarritoAndEstado(Carrito carrito, EstadoPedido estado);
}
