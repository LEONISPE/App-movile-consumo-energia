package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOpersona;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class PersonaController {

    private final PersonaService personaService;

    @Operation(
            summary = "Registrar miembros",
            description = "Permite registrar miembros a un hogar."
    )
    @PostMapping("/registrar/miembros/")
    public ResponseEntity<DTOpersona> crearMiembros(@RequestBody @Valid DTOpersona dtOpersona) {
        DTOpersona dtoPerssonaRespuesta = personaService.crearMiembrosHogar(dtOpersona);
        return ResponseEntity.ok(dtoPerssonaRespuesta);

    }

}
