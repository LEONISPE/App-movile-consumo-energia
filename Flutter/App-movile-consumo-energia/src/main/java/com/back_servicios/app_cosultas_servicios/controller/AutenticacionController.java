package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.UsuarioAutenticacionDTO;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.infra.security.DataJwToken;
import com.back_servicios.app_cosultas_servicios.infra.security.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/login")
public class AutenticacionController {

  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity authenticateUser(@RequestBody @Valid UsuarioAutenticacionDTO userAuthenticationDTO)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userAuthenticationDTO.email(), userAuthenticationDTO.password()
        );
        var authenticatedUser = authenticationManager.authenticate(authToken);
        var jwToken = tokenService.generateToken((Usuarios) authenticatedUser.getPrincipal());
        return ResponseEntity.ok(new DataJwToken(jwToken));
    }
}

