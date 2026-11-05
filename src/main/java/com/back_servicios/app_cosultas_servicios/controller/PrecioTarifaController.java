package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOprecioTarifa;
import com.back_servicios.app_cosultas_servicios.service.TarifaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * <h1>Controlador REST para la Gestión de Tarifas y Precios</h1>
 * <p>
 * Administra el registro de precios unitarios por servicio y los rangos de vigencia
 * aplicados en los cálculos de consumo. Restringido a usuarios con rol ADMIN.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */
@Tag(name = "Gestión de Tarifas", description = "Endpoints para registrar y modificar tarifas y precios unitarios de servicios")
@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class PrecioTarifaController {

    private final TarifaServicio tarifaServicio;

    /**
     * Actualiza los valores y periodos de vigencia de una tarifa existente.
     *
     * @param DTOprecioTarifa Objeto JSON con los nuevos datos de la tarifa.
     * @param idTarifa        {@code @PathVariable} Identificador único de la tarifa recibido directamente en la URI.
     * @return ResponseEntity con el DTO actualizado.
     */
    @Operation(
            summary = "actualizar tarifa de servicio",
            description = "Permite actualizar  la tarifa del servicio ."
    )
    @PutMapping("/actualizar/tarifa/{idTarifa}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DTOprecioTarifa> actualizarprecioTarifa(@RequestBody @Valid DTOprecioTarifa DTOprecioTarifa,
                                                             @PathVariable Long idTarifa) {
        DTOprecioTarifa dtoprecioRespuesta = tarifaServicio.modificarPrecioTarifaAgua( idTarifa,DTOprecioTarifa);
        return ResponseEntity.ok(dtoprecioRespuesta);

    }
    /**
     * Registra una nueva tarifa de precio para un servicio público.
     *
     * @param DTOprecioTarifa Objeto JSON con la información de la nueva tarifa a registrar.
     * @return ResponseEntity con el DTO registrado.
     */
    @Operation(
            summary = "Registrar tarifa de servicio",
            description = "Permite registrar  la tarifa del servicio ."
    )
    @PostMapping("/registrar/tarifa")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DTOprecioTarifa> crearprecioTarifa(@RequestBody @Valid DTOprecioTarifa DTOprecioTarifa) {
        DTOprecioTarifa dtoprecioRespuesta = tarifaServicio.crearTarifa(DTOprecioTarifa);
        return ResponseEntity.ok(dtoprecioRespuesta);

    }

}
