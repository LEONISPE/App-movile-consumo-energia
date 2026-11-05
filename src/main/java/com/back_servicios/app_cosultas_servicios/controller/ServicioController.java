package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOservicios;
import com.back_servicios.app_cosultas_servicios.service.ServiciosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * <h1>Controlador REST para la Gestión de Servicios Públicos</h1>
 * <p>
 * Expone los endpoints para la creación y administración del catálogo de servicios
 * disponibles en el sistema (ej. Agua, Energía, Gas). Requiere autenticación JWT y rol de Administrador.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */
@Tag(name = "Catálogo de Servicios", description = "Endpoints para la administración de tipos de servicios públicos")
@SecurityRequirement(name = "bearer-key")
@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class ServicioController {

    private final ServiciosService serviciosService;

    /**
     * Registra un nuevo servicio público en la plataforma.
     *
     * @param dtoservicios {@code @RequestBody} Estructura JSON con los datos del servicio.
     *                     {@code @Valid} Aplica las restricciones de validación declaradas en el DTO.
     * @return ResponseEntity con el DTO del servicio creado y código HTTP 200 OK.
     */
    @Operation(
            summary = "Registrar servicios",
            description = "Permite registrar  servicios."
    )
    @PostMapping("/registrar/servicios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DTOservicios> crearservicios(@RequestBody @Valid DTOservicios dtoservicios) {
        DTOservicios dtOserviciosRespuesta = serviciosService.crearServicios(dtoservicios);
        return ResponseEntity.ok(dtOserviciosRespuesta);

    }

}
