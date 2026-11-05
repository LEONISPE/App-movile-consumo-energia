package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOfactura;
import com.back_servicios.app_cosultas_servicios.service.FacturaService;
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
 * <h1>Controlador REST para la Gestión de Facturas</h1>
 * <p>
 * Permite el registro explícito de facturas y comprobantes de servicios asociados a los hogares.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */
@Tag(name = "Gestión de Facturas", description = "Endpoints para registro y control de facturas de servicios públicos")

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    /**
     * Registra una nueva factura en el sistema.
     *
     * @param dtOfactura Objeto JSON validado con los datos de consumo y costos.
     * @return ResponseEntity con mensaje de éxito.
     */
    @Operation(
            summary = "Registrar factura",
            description = "Permite registrar la factura del hogar."
    )
    @PostMapping("/registrar/factura")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registarfactura(@RequestBody @Valid DTOfactura dtOfactura) {
         facturaService.crearFactura(dtOfactura);
        return ResponseEntity.ok("Factura registrado exitosamente");

    }
}
