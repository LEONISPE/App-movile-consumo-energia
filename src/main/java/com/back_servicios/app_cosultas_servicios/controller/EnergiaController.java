package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioEnergia;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOConsumoAcomuladoEnergia;
import com.back_servicios.app_cosultas_servicios.service.EnergiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class EnergiaController {

    private final EnergiaService energiaService;

    @Operation(
            summary = "ver consumo de energia  en tiempo real",
            description = "Permite ver el consumo de la energia en el dia ."
    )
    @GetMapping("/energia/consumo")
    public ResponseEntity<ConsumoDTODiarioEnergia> verconsumoEnergia() {
        ConsumoDTODiarioEnergia consumoDTO = energiaService.calcularConsumoDiarioEnergia();
        return ResponseEntity.ok(consumoDTO);

    }

    @Operation(
            summary = "ver consumo de la energia acomulado",
            description = "permite ver el consumo de la  energia  acomulado y su costo" +
                    "desde el inicio del recibo hasta la fecha"
    )
    @GetMapping("/energia/consumo/acomulado")
    public ResponseEntity<DTOConsumoAcomuladoEnergia> verconsumoAcomuladoenergia() {
        DTOConsumoAcomuladoEnergia dtOconsumoAcomulado = energiaService.ObtenerConsumoAcomuladoEnergia();
        return ResponseEntity.ok(dtOconsumoAcomulado);
    }

}
