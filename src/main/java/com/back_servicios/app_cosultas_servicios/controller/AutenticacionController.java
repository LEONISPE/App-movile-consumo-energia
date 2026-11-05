package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.UsuarioAutenticacionDTO;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.infra.security.DataJwToken;
import com.back_servicios.app_cosultas_servicios.infra.security.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>Controlador REST para la Autenticación y Seguridad</h1>
 * <p>
 * Gestiona el proceso de inicio de sesión (*login*), autenticando las credenciales del usuario
 * y generando el Token JWT de acceso cifrado para las peticiones posteriores.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */
@Tag(name = "Autenticación", description = "Endpoint público para autenticación de usuarios y generación de tokens JWT")

@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class AutenticacionController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    /**
     * Autentica las credenciales (correo y contraseña) del usuario y retorna un token JWT válido.
     *
     * @param usuarioAutenticacionDTO Objeto JSON con el email y contraseña de acceso.
     * @return ResponseEntity con el objeto {@link DataJwToken} que contiene el token JWT generado.
     */
    @PostMapping
    public ResponseEntity authenticateUser(@RequestBody @Valid UsuarioAutenticacionDTO usuarioAutenticacionDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                usuarioAutenticacionDTO.email(), usuarioAutenticacionDTO.password()
        );
        var authenticatedUser = authenticationManager.authenticate(authToken);
        var jwToken = tokenService.generateToken((Usuarios) authenticatedUser.getPrincipal());
        return ResponseEntity.ok(new DataJwToken(jwToken));
    }
}