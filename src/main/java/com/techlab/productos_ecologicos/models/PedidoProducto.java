package com.techlab.productos_ecologicos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido_productos")
public class PedidoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Pedido al que pertenece este detalle.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // Producto comprado.
    //
    // Se mantiene la referencia al producto original para
    // poder identificar qué producto fue comprado.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Cantidad de unidades compradas.
    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer cantidad;

    // Precio del producto en el momento de la compra.
    //
    // No debemos depender del precio actual de Producto,
    // porque puede cambiar después de realizada la compra.
    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Double precioUnitario;
}