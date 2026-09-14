package com.techlab.productos_ecologicos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"}),
    @UniqueConstraint(columnNames = {"email"})
})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Username utilizado para iniciar sesión.
    @Column(nullable = false)
    private String username;

    // Email utilizado para confirmación de cuenta
    // y posteriormente para autenticación con Google.
    private String email;

    // Contraseña encriptada con BCrypt.
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String nombre;
    private String apellido;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Indica si la cuenta está habilitada.
    //
    // Registro tradicional:
    // false → email pendiente de confirmación.
    // true  → email confirmado.
    //
    // Google:
    // true → cuenta considerada verificada por Google.
    @Builder.Default
    private Boolean enabled = true;

    /*
     * No incluir carritos en toString().
     *
     * La colección es administrada por Hibernate y puede
     * encontrarse en estado LAZY. Si Lombok intenta acceder
     * a ella fuera de una sesión Hibernate, puede producir
     * LazyInitializationException.
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "usuario")
    private List<Carrito> carritos;

    // Devuelve los roles del usuario.
    // Spring Security los utiliza para determinar
    // qué operaciones puede realizar.
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
            new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    // La cuenta está habilitada únicamente cuando
    // el campo enabled es true.
    //
    // Esto permitirá bloquear el login de usuarios
    // que todavía no hayan confirmado su email.
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Los siguientes estados todavía no tienen
    // una lógica específica en el proyecto.
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}