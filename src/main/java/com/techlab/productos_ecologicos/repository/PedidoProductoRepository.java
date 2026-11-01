package com.techlab.productos_ecologicos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.productos_ecologicos.models.Pedido;
import com.techlab.productos_ecologicos.models.PedidoProducto;

public interface PedidoProductoRepository extends JpaRepository<PedidoProducto, Integer> {

    // Obtiene todos los productos asociados a un pedido.
    List<PedidoProducto> findByPedido(Pedido pedido);

}