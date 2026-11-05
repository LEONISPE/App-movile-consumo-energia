package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioEnergia;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOConsumoAcomuladoEnergia;
import com.back_servicios.app_cosultas_servicios.service.EnergiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>Controlador REST para el Monitoreo de Energía Eléctrica</h1>
 * <p>
 * Expone los endpoints para consultar el consumo diario en tiempo real y el acumulado
 * del servicio de energía eléctrica del hogar autenticado.
 * </p>
 *
 * @author SQA Team - Evidencia 4
 * @version 1.0
 */
@Tag(name = "Monitoreo de Energía", description = "Endpoints para consultar consumos diarios y acumulados de energía eléctrica")



@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class EnergiaController {

    private final EnergiaService energiaService;

    /**
     * Consulta el consumo de energía estimado en tiempo real para el día actual.
     *
     * @return DTO con el consumo diario de energía y costo base.
     */
    @Operation(
            summary = "ver consumo de energia  en tiempo real",
            description = "Permite ver el consumo de la energia en el dia ."
    )
    @GetMapping("/energia/consumo")
    public ResponseEntity<ConsumoDTODiarioEnergia> verconsumoEnergia() {
        ConsumoDTODiarioEnergia consumoDTO = energiaService.calcularConsumoDiarioEnergia();
        return ResponseEntity.ok(consumoDTO);

    }

    /**
     * Consulta el consumo acumulado de energía desde el inicio del ciclo de facturación.
     *
     * @return DTO con el consumo acumulado y su costo total.
     */
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
