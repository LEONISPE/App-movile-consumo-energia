package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioAgua;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOconsumoAcomulado;
import com.back_servicios.app_cosultas_servicios.service.AguaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class AguaController {

    private final AguaService aguaService;


    @Operation(
            summary = "ver consumo de agua  en tiempo real",
            description = "Permite ver el consumo del agua en el dia ."
    )
    @GetMapping("/agua/consumo")
    public ResponseEntity<ConsumoDTODiarioAgua> verconsumoAgua() {
        ConsumoDTODiarioAgua consumoDTO = aguaService.calcularConsumoActual();
        return ResponseEntity.ok(consumoDTO);

    }

    @Operation(
            summary = "ver consumo del agua acomulado",
            description = "permite ver el consumo del agua acomulado y su costo" +
                    "desde el inicio del recibo hasta la fecha"
    )
    @GetMapping("/agua/consumo/acomulado")
    public ResponseEntity<DTOconsumoAcomulado> verconsumoAcomuladoAgua() {
        DTOconsumoAcomulado dtOconsumoAcomulado = aguaService.obtenerConsumoAcomulado();
        return ResponseEntity.ok(dtOconsumoAcomulado);
    }

}
