package com.techlab.productos_ecologicos.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Data //Generea getters, setters, ToSting ,equals
@AllArgsConstructor //Genera un constructor con todos los campos.
@NoArgsConstructor //Genera un constructor sin argumentos.

@Entity
@Table(name = "producto")

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "El precio del producto no puede estar vacío")
    @Positive(message = "El precio debe ser mayor que cero")
    @Column(name = "precio", nullable = false)
    private Double precio;

    @NotBlank(message = "La descripción del producto no puede estar vacía")
    @Column(name = "descripcion", length = 200)
    private String descripcion;

    
    @PositiveOrZero(message = "El stock no puede ser un número negativo")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @NotBlank(message = "Por favor, selecciona una imagen para continuar")
    @Column(name = "imagen_url", length = 200)
    private String imagenUrl;

    @NotNull(message = "El campo destacado no puede ser nulo")
    @Column(name= "destacado", nullable = false)
    private Boolean destacado = false;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}