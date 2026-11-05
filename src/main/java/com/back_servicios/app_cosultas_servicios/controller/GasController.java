package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioAgua;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioGas;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTOacomuladoGas;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOconsumoAcomuladoAgua;
import com.back_servicios.app_cosultas_servicios.service.AguaService;
import com.back_servicios.app_cosultas_servicios.service.GasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <h1>Controlador REST para el Monitoreo de Gas</h1>
 * <p>
 * Expone los endpoints para consultar el consumo en tiempo real (diario) y el consumo acumulado
 * del servicio de gas domiciliario para el hogar en sesión.
 * </p>
 *
 * @author SQA Team - Evidencia 4
 * @version 1.0
 */
@Tag(name = "Monitoreo de Gas", description = "Endpoints para consultar consumos diarios y acumulados de gas")

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class GasController {

    private final GasService gasService;


    /**
     * Consulta el consumo de gas estimado en tiempo real para el día actual.
     *
     * @return DTO con el consumo diario, costo base y tasa por hora.
     */
    @Operation(
            summary = "ver consumo de Gas  en tiempo real",
            description = "Permite ver el consumo del Gas en el dia ."
    )
    @GetMapping("/gas/consumo")
    public ResponseEntity<ConsumoDTODiarioGas> verconsumoGas() {
        ConsumoDTODiarioGas consumoDTO = gasService.calcularConsumoActual();
        return ResponseEntity.ok(consumoDTO);

    }
    /**
     * Consulta el consumo acumulado de gas desde el inicio del período de facturación.
     *
     * @return DTO con el consumo acumulado total y su costo monetario asociado.
     */

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
