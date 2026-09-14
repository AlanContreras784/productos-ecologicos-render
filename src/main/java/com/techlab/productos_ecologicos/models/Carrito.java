package com.techlab.productos_ecologicos.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carrito")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EstadoCarrito estado;
    
    // OneToMany: un carrito tiene muchos CarritoProducto.
    // mappedBy indica que la relación está definida en CarritoProducto (campo "carrito").
    // CascadeType.ALL: si se elimina el carrito, se eliminan sus CarritoProducto.
    // orphanRemoval: si se quita un CarritoProducto de la lista, se elimina de la base.
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoProducto> productos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}