package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOfactura;
import com.back_servicios.app_cosultas_servicios.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @Operation(
            summary = "Registrar factura",
            description = "Permite registrar la factura del hogar."
    )
    @PostMapping("/registrar/factura")
    public ResponseEntity<String> registarfactura(@RequestBody @Valid DTOfactura dtOfactura) {
         facturaService.crearFactura(dtOfactura);
        return ResponseEntity.ok("Factura registrado exitosamente");

    }
}
