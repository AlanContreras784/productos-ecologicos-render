package com.techlab.productos_ecologicos.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Usuario que realizó la compra.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Carrito exacto que originó este pedido.
    //
    // El carrito ya tiene reservado el stock de los productos.
    // Por eso no debemos descontar stock nuevamente al confirmar
    // la compra.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false, unique = true)
    private Carrito carrito;

    // Fecha y hora en que se creó el pedido.
    @Column(nullable = false)
    private LocalDateTime fecha;

    // Estado actual del pedido.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    // Datos de envío registrados en el momento de la compra.
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 30)
    private String telefono;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 100)
    private String provincia;

    @Column(nullable = false, length = 20)
    private String codigoPostal;

    // Importes registrados al momento de realizar la compra.
    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Double subtotal;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Double envio;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Double total;

    // Productos incluidos en el pedido.
    @OneToMany(
        mappedBy = "pedido",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<PedidoProducto> productos = new ArrayList<>();
}