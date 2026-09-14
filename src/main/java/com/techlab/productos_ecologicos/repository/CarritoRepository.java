package com.techlab.productos_ecologicos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.productos_ecologicos.models.Carrito;
import com.techlab.productos_ecologicos.models.EstadoCarrito;
import com.techlab.productos_ecologicos.models.Usuario;

public interface  CarritoRepository extends JpaRepository<Carrito, Integer> {
    Optional<Carrito> findByUsuarioAndEstado(Usuario usuario, EstadoCarrito estado);
}
