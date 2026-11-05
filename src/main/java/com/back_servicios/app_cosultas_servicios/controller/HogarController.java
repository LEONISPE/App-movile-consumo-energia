package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOmiebrosHogar;
import com.back_servicios.app_cosultas_servicios.service.HogarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h1>Controlador REST para la Gestión de Hogares</h1>
 * <p>
 * Administra el registro de hogares en el sistema y permite consultar los miembros
 * y factores demográficos asociados al grupo familiar autenticado.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */
@Tag(name = "Gestión de Hogares", description = "Endpoints para registro de hogares y consulta de integrantes familiares")
@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class HogarController {

private final HogarService hogarService;

    /**
     * Registra un nuevo hogar en el sistema.
     *
     * @param dtohogar Objeto JSON con los datos del hogar y estratos.
     * @return ResponseEntity con el DTO del hogar creado.
     */
    @Operation(
            summary = "Registrar hogar",
            description = "Permite registrar el hogar a el dueño."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registrar/hogar/")
    public ResponseEntity<DTOhogar> crearHogar(@RequestBody @Valid DTOhogar dtohogar) {
        DTOhogar dtohogarRespuesta = hogarService.createHogar(dtohogar);
        return ResponseEntity.ok(dtohogarRespuesta);

    }
    /**
     * Consulta la lista de miembros pertenecientes al hogar del usuario autenticado.
     *
     * @return Lista de DTOs con la información de los miembros del hogar.
     */
    @Operation(
            summary = "ver miembros del  hogar",
            description = "Permite ver miembros del hogar por el id del hogar ."
    )
    @GetMapping("/ver/miembros")
    public ResponseEntity<List<DTOmiebrosHogar>> verMiembrosHogar() {
        List<DTOmiebrosHogar> miembros = hogarService.obtenerMiembrosDelHogar();
        return ResponseEntity.ok(miembros);

    }

}
