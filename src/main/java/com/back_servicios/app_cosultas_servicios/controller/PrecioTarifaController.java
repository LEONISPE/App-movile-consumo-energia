package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOprecioTarifa;
import com.back_servicios.app_cosultas_servicios.service.TarifaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class PrecioTarifaController {

    private final TarifaServicio tarifaServicio;

    @Operation(
            summary = "actualizar tarifa de servicio",
            description = "Permite actualizar  la tarifa del servicio ."
    )
    @PutMapping("/actualizar/tarifa/{idTarifa}")
    public ResponseEntity<DTOprecioTarifa> actualizarprecioTarifa(@RequestBody @Valid DTOprecioTarifa DTOprecioTarifa,
                                                             @PathVariable Long idTarifa) {
        DTOprecioTarifa dtoprecioRespuesta = tarifaServicio.modificarPrecioTarifaAgua( idTarifa,DTOprecioTarifa);
        return ResponseEntity.ok(dtoprecioRespuesta);

    }
    @Operation(
            summary = "Registrar tarifa de servicio",
            description = "Permite registrar  la tarifa del servicio ."
    )
    @PutMapping("/registrar/tarifa")
    public ResponseEntity<DTOprecioTarifa> crearprecioTarifa(@RequestBody @Valid DTOprecioTarifa DTOprecioTarifa) {
        DTOprecioTarifa dtoprecioRespuesta = tarifaServicio.crearTarifaAgua(DTOprecioTarifa);
        return ResponseEntity.ok(dtoprecioRespuesta);

    }

}
