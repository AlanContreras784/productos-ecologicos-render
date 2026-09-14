package com.techlab.productos_ecologicos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrito_productos")
public class CarritoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación con Carrito — muchos CarritoProducto pertenecen a un Carrito
    @ManyToOne
    @JoinColumn(name = "carrito_id")
    @JsonIgnore
    private Carrito carrito;

    // Relación con Producto — muchos CarritoProducto apuntan a un Producto
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    // Atributo propio de la relación.
    // Esto es lo que no podíamos tener con @ManyToMany directo.
    @Column(nullable = false)
    private Integer cantidad;
}