package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOconsumoServicio;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.service.ConsumoServicio;
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
public class ConsumoServicioController {

    private final ConsumoServicio consumoServicio;

    @Operation(
            summary = "Registrar consumo servicio",
            description = "Permite registrar el consumo  de servicio a el hogar ."
    )
    @PostMapping("/registrar/ConsumoServicio")
    public ResponseEntity<DTOconsumoServicio> crearConsumoServicio(@RequestBody @Valid DTOconsumoServicio dtOconsumoServicio) {
        DTOconsumoServicio dtoservicioRespuesta = consumoServicio.crearDTOconsumoServicio(dtOconsumoServicio);
        return ResponseEntity.ok(dtoservicioRespuesta);

    }
}
