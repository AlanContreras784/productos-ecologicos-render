package com.techlab.productos_ecologicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import com.techlab.productos_ecologicos.models.Categoria;
import com.techlab.productos_ecologicos.models.Producto;
import com.techlab.productos_ecologicos.services.CategoriaService;
import com.techlab.productos_ecologicos.services.ProductoService;

import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class ProductosEcologicosApplication {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    public static void main(String[] args) {
        SpringApplication.run(ProductosEcologicosApplication.class, args);
    }

    @Bean
    CommandLineRunner verificarConfiguracionMail() {
        return args -> {

            System.out.println("======================================");
            System.out.println("VERIFICACIÓN CONFIGURACIÓN SMTP");
            System.out.println("MAIL USERNAME: " + mailUsername);
            System.out.println("MAIL PASSWORD CONFIGURADO: "
                    + (mailPassword != null && !mailPassword.isBlank()));
            System.out.println("MAIL PASSWORD LONGITUD: "
                    + (mailPassword != null ? mailPassword.length() : 0));
            System.out.println("======================================");
        };
    }

    @Bean
    CommandLineRunner cargarDatos(
            CategoriaService categoriaService,
            ProductoService productoService) {

        return args -> {

            if (categoriaService.listarTodos().isEmpty()) {

                Categoria bolsasResiduos = categoriaService.guardar(
                    new Categoria(
                        null,
                        "Bolsas de residuos",
                        "Bolsas y cestos para separar y reducir residuos en el hogar"
                    )
                );

                Categoria luminarias = categoriaService.guardar(
                    new Categoria(
                        null,
                        "Luminarias",
                        "Luminarias solares y de bajo consumo para el hogar"
                    )
                );

                Categoria biodegradables = categoriaService.guardar(
                    new Categoria(
                        null,
                        "Biodegradables",
                        "Productos que se degradan naturalmente sin dejar residuos contaminantes"
                    )
                );

                Categoria reutilizables = categoriaService.guardar(
                    new Categoria(
                        null,
                        "Reutilizables",
                        "Artículos pensados para usarse muchas veces en reemplazo de descartables"
                    )
                );

                // -----------------------------------------
                // Bolsas de residuos
                // -----------------------------------------

                productoService.guardar(new Producto(
                    null,
                    "Bolsas de residuos biodegradables (pack chico)",
                    6.99,
                    "Bolsas de residuos biodegradables, pack chico",
                    45,
                    "https://i.postimg.cc/D8Y7Zb9D/bolsasderesiduos-pequena.jpg",
                    false,
                    bolsasResiduos
                ));

                productoService.guardar(new Producto(
                    null,
                    "Cesto dual para separación de residuos",
                    24.99,
                    "Cesto dual para separación de residuos",
                    15,
                    "https://i.postimg.cc/N955VFZp/cesto-Dual.jpg",
                    false,
                    bolsasResiduos
                ));

                // -----------------------------------------
                // Luminarias
                // -----------------------------------------

                productoService.guardar(new Producto(
                    null,
                    "Luminaria solar de tres lámparas",
                    34.99,
                    "Luminaria solar de tres lámparas",
                    12,
                    "https://i.postimg.cc/bD8hM0n6/luminaria-Tres-Lamparas.jpg",
                    false,
                    luminarias
                ));

                productoService.guardar(new Producto(
                    null,
                    "Luminaria solar de una lámpara",
                    19.99,
                    "Luminaria solar de una lámpara",
                    20,
                    "https://i.postimg.cc/zb5rMwhR/luminaria-Una-Lampara.jpg",
                    false,
                    luminarias
                ));

                productoService.guardar(new Producto(
                    null,
                    "Velador de escritorio bajo consumo",
                    16.50,
                    "Velador de escritorio bajo consumo",
                    18,
                    "https://i.postimg.cc/Th38D3JL/velador-Escritorio.jpg",
                    true,
                    luminarias
                ));

                productoService.guardar(new Producto(
                    null,
                    "Lámparas LED de bajo consumo",
                    12.99,
                    "Lámparas LED de bajo consumo",
                    30,
                    "https://i.postimg.cc/WqNsDmbr/lamparas-Led.jpg",
                    false,
                    luminarias
                ));

                // -----------------------------------------
                // Biodegradables
                // -----------------------------------------

                productoService.guardar(new Producto(
                    null,
                    "Ecomaceta pequeña biodegradable",
                    5.50,
                    "Ecomaceta pequeña biodegradable",
                    40,
                    "https://i.postimg.cc/bsvzpRB9/ecomacetas-pequena.jpg",
                    false,
                    biodegradables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Maceta plantable biodegradable",
                    4.99,
                    "Maceta plantable biodegradable",
                    38,
                    "https://i.postimg.cc/hJVnpZXv/maceta-Plantable.jpg",
                    true,
                    biodegradables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Lápiz plantable (germina al enterrarlo)",
                    2.99,
                    "Lápiz plantable (germina al enterrarlo)",
                    70,
                    "https://i.postimg.cc/9Rphj4PS/lapiz-Plantable.jpg",
                    true,
                    biodegradables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Anotadores de papel reciclado",
                    4.50,
                    "Anotadores de papel reciclado",
                    50,
                    "https://i.postimg.cc/bsjw0mY6/anotadores.jpg",
                    false,
                    biodegradables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Lápices de colores de madera certificada",
                    5.99,
                    "Lápices de colores de madera certificada",
                    45,
                    "https://i.postimg.cc/RNQVgwNv/lapices-Colores.png",
                    false,
                    biodegradables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Kit de siembra para huerta en casa",
                    15.99,
                    "Kit de siembra para huerta en casa",
                    22,
                    "https://i.postimg.cc/mzzgjqFn/kit-De-Siembra.jpg",
                    true,
                    biodegradables
                ));

                // -----------------------------------------
                // Reutilizables
                // -----------------------------------------

                productoService.guardar(new Producto(
                    null,
                    "Bolsa reutilizable de tela modelo clásico",
                    9.99,
                    "Bolsa reutilizable de tela modelo clásico",
                    40,
                    "https://i.postimg.cc/PpdgKXGy/bolsa-Reutilizable.jpg",
                    false,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Bolsa reutilizable de tela modelo mediano",
                    10.99,
                    "Bolsa reutilizable de tela modelo mediano",
                    35,
                    "https://i.postimg.cc/r0gvPBcB/bolsa-Reutilizable-2.jpg",
                    false,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Bolsa reutilizable de tela modelo grande",
                    12.50,
                    "Bolsa reutilizable de tela modelo grande",
                    30,
                    "https://i.postimg.cc/1nykMmhh/bolsa-Reutilizable-3.jpg",
                    false,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Bolsas Rock & Vida edición especial",
                    13.99,
                    "Bolsas Rock & Vida edición especial",
                    20,
                    "https://i.postimg.cc/NyWcs05N/bolsas-Rock-amp-vida.jpg",
                    true,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Botella Cero Huella acero inoxidable",
                    18.99,
                    "Botella Cero Huella acero inoxidable",
                    25,
                    "https://i.postimg.cc/Tyj8rysn/botella-Cero-Huella.jpg",
                    false,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Botella Cero Huella modelo CH",
                    19.99,
                    "Botella Cero Huella modelo CH",
                    20,
                    "https://i.postimg.cc/LJn6BVkj/Botellas-CH.jpg",
                    false,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Set de botellas reutilizables",
                    22.50,
                    "Set de botellas reutilizables",
                    18,
                    "https://i.postimg.cc/1g1hn0vt/botellas-Reutilizables.jpg",
                    true,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Taza Cero Huella",
                    11.99,
                    "Taza Cero Huella",
                    30,
                    "https://i.postimg.cc/4HdrkWx9/taza-Cero-Huella-2.jpg",
                    true,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Vaso Cero Huella 300cc",
                    7.99,
                    "Vaso Cero Huella 300cc",
                    45,
                    "https://i.postimg.cc/G9m0ymxb/vaso-Cerohuella-De300cc.jpg",
                    true,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Vaso Cero Huella 500cc",
                    9.50,
                    "Vaso Cero Huella 500cc",
                    40,
                    "https://i.postimg.cc/hXzc7tgM/vaso-Cero-Huella-De500cc.jpg",
                    false,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Kit de cubiertos reutilizables para llevar",
                    8.75,
                    "Kit de cubiertos reutilizables para llevar",
                    33,
                    "https://i.postimg.cc/cr6NVLvM/kit-De-Cubiertos.jpg",
                    true,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Vasos térmicos reutilizables",
                    15.99,
                    "Vasos térmicos reutilizables",
                    25,
                    "https://i.postimg.cc/XrG4yY3X/vasos-Termicos.jpg",
                    true,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Pico auto-regable para macetas",
                    6.99,
                    "Pico auto-regable para macetas",
                    28,
                    "https://i.postimg.cc/NK3Y8vfz/pico-Auto-Regable2.jpg",
                    false,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Lapicera ecológica de material reciclado",
                    3.20,
                    "Lapicera ecológica de material reciclado",
                    60,
                    "https://i.postimg.cc/kDJCvcqd/lapicera.jpg",
                    false,
                    reutilizables
                ));

                productoService.guardar(new Producto(
                    null,
                    "Lapicera ecológica ECO",
                    3.50,
                    "Lapicera ecológica ECO de material reciclado",
                    55,
                    "https://i.postimg.cc/1nZmShkW/Lapicera-ECO.jpg",
                    false,
                    reutilizables
                ));
            }
        };
    }
}