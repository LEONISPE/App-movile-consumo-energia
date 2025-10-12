package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOusuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.service.HogarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class HogarController {

private final HogarService hogarService;

    @Operation(
            summary = "Registrar hogar",
            description = "Permite registrar el hogar a el dueño."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registrar/hogar/{idUsuario}")
    public ResponseEntity<DTOhogar> crearHogar(@RequestBody @Valid DTOhogar dtohogar, @PathVariable Long idUsuario) {
        DTOhogar dtohogarRespuesta = hogarService.createHogar(dtohogar, idUsuario);
        return ResponseEntity.ok(dtohogarRespuesta);

    }

}
