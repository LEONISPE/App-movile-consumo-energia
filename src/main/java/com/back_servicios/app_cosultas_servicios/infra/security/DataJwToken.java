package com.back_servicios.app_cosultas_servicios.infra.security;

/**
 * Objeto de transferencia (Record) que encapsula el JSON Web Token (JWT)
 * generado tras una autenticación exitosa para ser retornado al cliente en el proceso de login.
 */
public record DataJwToken(String jwToken) {
}
