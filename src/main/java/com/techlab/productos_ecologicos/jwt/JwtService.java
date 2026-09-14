package com.techlab.productos_ecologicos.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Clave secreta obtenida desde application.properties.
    // El valor real se encuentra en la variable de entorno JWT_SECRET.
    @Value("${jwt.secret}")
    private String secretKey;

    // Tiempo de expiración del token obtenido desde application.properties.
    // Actualmente está configurado en 24 horas.
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Genera un token con los datos básicos del usuario.
    public String generarToken(UserDetails usuario) {
        return generarToken(new HashMap<>(), usuario);
    }

    // Genera un token con claims adicionales (datos extra a incluir en el payload).
    public String generarToken(Map<String, Object> claimsExtra, UserDetails usuario) {
        return Jwts.builder()
                .claims(claimsExtra)
                // subject: identificador principal — el username queda en el payload como "sub"
                .subject(usuario.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                // Expiración configurada desde application.properties.
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getKey())
                .compact(); // genera el string eyJ...
    }

    // Construye la clave criptográfica a partir de la clave secreta en Base64.
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extrae el username del payload del token.
    public String obtenerUsername(String token) {
        return obtenerClaim(token, Claims::getSubject);
    }

    // Valida que el token sea correcto y corresponda al usuario dado.
    // Es válido si: el username coincide Y el token no expiró.
    public boolean esTokenValido(String token, UserDetails usuario) {
        final String username = obtenerUsername(token);
        return username.equals(usuario.getUsername()) && !estaExpirado(token);
    }

    // Obtiene todos los claims del token verificando la firma contra la clave secreta.
    // Si la firma no coincide, lanza una excepción.
    private Claims obtenerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Obtiene un claim específico usando una función como parámetro.
    // Permite extraer cualquier campo del token con el mismo método:
    // obtenerClaim(token, Claims::getSubject) → devuelve el username
    // obtenerClaim(token, Claims::getExpiration) → devuelve la fecha de expiración
    public <T> T obtenerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = obtenerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    // Obtiene la fecha de expiración del token.
    private Date obtenerExpiracion(String token) {
        return obtenerClaim(token, Claims::getExpiration);
    }

    // Devuelve true si la fecha de expiración del token es anterior a la fecha actual.
    private boolean estaExpirado(String token) {
        return obtenerExpiracion(token).before(new Date());
    }
}