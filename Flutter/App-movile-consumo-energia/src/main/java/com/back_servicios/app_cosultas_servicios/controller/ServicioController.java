package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOservicios;
import com.back_servicios.app_cosultas_servicios.service.ServiciosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class ServicioController {

    private final ServiciosService serviciosService;

    @Operation(
            summary = "Registrar servicios",
            description = "Permite registrar  servicios."
    )
    @PostMapping("/registrar/servicios")
    public ResponseEntity<DTOservicios> crearservicios(@RequestBody @Valid DTOservicios dtoservicios) {
        DTOservicios dtOserviciosRespuesta = serviciosService.crearServicios(dtoservicios);
        return ResponseEntity.ok(dtOserviciosRespuesta);

    }

}
