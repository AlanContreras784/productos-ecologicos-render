package com.techlab.productos_ecologicos.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.productos_ecologicos.models.Carrito;
import com.techlab.productos_ecologicos.models.CarritoProducto;
import com.techlab.productos_ecologicos.models.Producto;

import java.util.Optional;

public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Integer> {

    // Busca si ya existe una fila con ese carrito y ese producto.
    // Si existe, incrementamos la cantidad. Si no, creamos una nueva fila.
    Optional<CarritoProducto> findByCarritoAndProducto(Carrito carrito, Producto producto);
}
