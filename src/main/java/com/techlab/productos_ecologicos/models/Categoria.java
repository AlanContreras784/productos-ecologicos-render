package com.techlab.productos_ecologicos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Generea getters, setters, ToSting ,equals
@NoArgsConstructor
@AllArgsConstructor

@Entity //Indica que la clase es una entidad de JPA y se mapeará a una tabla en la base de datos.
@Table(name = "categoria")

public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La descripción de la categoría no puede estar vacía")
    @Column(name = "descripcion", length = 200)
    private String descripcion;
    
}
