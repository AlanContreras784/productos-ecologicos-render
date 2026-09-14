package com.techlab.productos_ecologicos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.productos_ecologicos.models.Producto;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreContaining(String nombre);

//     @Query("SELECT p FROM Producto p WHERE p.categoria.nombre = :nombreCategoria")
//     List<Producto> buscarPorCategoria(@Param("nombreCategoria") String nombreCategoria);

    List<Producto> findByCategoriaNombreContainingIgnoreCase(String nombreCategoria);
    List<Producto> findByDestacadoTrue();
 }
