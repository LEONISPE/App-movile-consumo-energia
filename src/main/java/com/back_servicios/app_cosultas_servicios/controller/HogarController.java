package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOmiebrosHogar;
import com.back_servicios.app_cosultas_servicios.service.HogarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class HogarController {

private final HogarService hogarService;

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
