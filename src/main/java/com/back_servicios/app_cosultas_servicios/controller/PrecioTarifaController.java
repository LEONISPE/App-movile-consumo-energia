package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOconsumoServicio;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOprecioTarifa;
import com.back_servicios.app_cosultas_servicios.service.ConsumoServicio;
import com.back_servicios.app_cosultas_servicios.service.TarifaServicio;
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
public class PrecioTarifaController {

    private final TarifaServicio tarifaServicio;

    @Operation(
            summary = "Registrar tarifa de servicio",
            description = "Permite registrar  la tarifa del servicio ."
    )
    @PostMapping("/registrar/tarifa")
    public ResponseEntity<DTOprecioTarifa> crearprecioTarifa(@RequestBody @Valid DTOprecioTarifa DTOprecioTarifa) {
        DTOprecioTarifa dtoprecioRespuesta = tarifaServicio.cargarPrecioTarifa(DTOprecioTarifa);
        return ResponseEntity.ok(dtoprecioRespuesta);

    }
}
