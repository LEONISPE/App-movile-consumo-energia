package com.back_servicios.app_cosultas_servicios.infra.security;


import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;

import java.time.*;

/**
 * Servicio encargado de la gestión criptográfica y generación de JSON Web Tokens (JWT).
 * Utiliza el algoritmo HMAC256 para firmar los tokens, incorpora claims personalizados
 * (ID de usuario, rol y correo) y administra tiempos de expiración sincronizados con la zona horaria de Colombia.
 */

@Service
public class TokenService {

    @Value("${api.infra.security.secret}")
    private String apiSecret;

    private static final ZoneId ZONA_COLOMBIA = ZoneId.of("America/Bogota");

    /** Genera un token JWT firmado para el usuario autenticado con duración de 60 minutos. */
    public String generateToken(Usuarios user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create() // <-- CORREGIDO: lleva paréntesis ()
                    .withIssuer("Back")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getIdUsuario())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(generateExpiryDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error al generar el token");
        }
    }

    private Instant generateExpiryDate() {
        return ZonedDateTime.now(ZONA_COLOMBIA).plusMinutes(60).toInstant();
    }

    /** Extrae y valida el subject (email) contenido dentro del token JWT. */
    public String getSubject(String token) {
        if (token == null) {
            throw new RuntimeException("Token es nulo");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            DecodedJWT verifier = JWT.require(algorithm)
                    .withIssuer("Back")
                    .build()
                    .verify(token);

            return verifier.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException("Token inválido o expirado", exception);
        }
    }

    /** Extrae el rol del usuario desde los claims del token. */
    public String getRoleFromToken(String token) {
        if (token == null) {
            throw new RuntimeException("Token is null");
        }
        Algorithm algorithm = Algorithm.HMAC256(apiSecret);
        DecodedJWT verifier = JWT.require(algorithm)
                .withIssuer("Back")
                .build()
                .verify(token);
        return verifier.getClaim("role").asString();
    }

    /** Extrae el ID del usuario desde los claims del token. */
    public Long getUserIdFromToken(String token) {
        if (token == null) {
            throw new RuntimeException("Token is null");
        }
        Algorithm algorithm = Algorithm.HMAC256(apiSecret);
        DecodedJWT verifier = JWT.require(algorithm)
                .withIssuer("Back")
                .build()
                .verify(token);
        return verifier.getClaim("id").asLong();
    }

    /** Valida y decodifica el token completo retornando el objeto DecodedJWT. */
    public DecodedJWT validateAndDecodeToken(String token) {
        if (token == null) {
            throw new RuntimeException("Token is null");
        }
        Algorithm algorithm = Algorithm.HMAC256(apiSecret);
        return JWT.require(algorithm)
                .withIssuer("Back")
                .build()
                .verify(token);
    }
}

