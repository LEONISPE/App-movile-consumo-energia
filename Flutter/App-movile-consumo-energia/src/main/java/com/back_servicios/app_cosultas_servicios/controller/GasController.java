package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioAgua;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioGas;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTOacomuladoGas;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOconsumoAcomuladoAgua;
import com.back_servicios.app_cosultas_servicios.service.AguaService;
import com.back_servicios.app_cosultas_servicios.service.GasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class GasController {

    private final GasService gasService;


    @Operation(
            summary = "ver consumo de Gas  en tiempo real",
            description = "Permite ver el consumo del Gas en el dia ."
    )
    @GetMapping("/gas/consumo")
    public ResponseEntity<ConsumoDTODiarioGas> verconsumoGas() {
        ConsumoDTODiarioGas consumoDTO = gasService.calcularConsumoActual();
        return ResponseEntity.ok(consumoDTO);

    }

    @Operation(
            summary = "ver consumo del Gas acomulado",
            description = "permite ver el consumo del Gas acomulado y su costo" +
                    "desde el inicio del recibo hasta la fecha"
    )
    @GetMapping("/gas/consumo/acomulado")
    public ResponseEntity<ConsumoDTOacomuladoGas> verconsumoAcomuladoGas() {
        ConsumoDTOacomuladoGas dtOconsumoAcomuladogas = gasService.obtenerConsumoAcomulado();
        return ResponseEntity.ok(dtOconsumoAcomuladogas);
    }

}
