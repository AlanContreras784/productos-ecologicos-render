package com.techlab.productos_ecologicos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techlab.productos_ecologicos.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Busca productos cuyo nombre contiene el texto indicado.
    List<Producto> findByNombreContaining(String nombre);

    // Busca productos por el nombre de su categoría.
    List<Producto> findByCategoriaNombreContainingIgnoreCase(String nombreCategoria);

    // Busca los productos destacados.
    List<Producto> findByDestacadoTrue();

    // Reserva stock si hay suficientes unidades disponibles.
    @Modifying
    @Query("""
        UPDATE Producto p
        SET p.stock = p.stock - :cantidad
        WHERE p.id = :productoId
          AND p.stock >= :cantidad
        """)
    int reservarStockSiDisponible(
            @Param("productoId") Integer productoId,
            @Param("cantidad") Integer cantidad);


    // Devuelve al stock las unidades de un producto.
    @Modifying
    @Query("""
        UPDATE Producto p
        SET p.stock = p.stock + :cantidad
        WHERE p.id = :productoId
        """)
    int devolverStock(
            @Param("productoId") Integer productoId,
            @Param("cantidad") Integer cantidad);
}
