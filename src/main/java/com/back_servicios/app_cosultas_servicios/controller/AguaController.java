package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioAgua;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOconsumoAcomuladoAgua;
import com.back_servicios.app_cosultas_servicios.domain.entity.Factura;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import com.back_servicios.app_cosultas_servicios.service.AguaService;
import com.back_servicios.app_cosultas_servicios.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class AguaController {

    private final AguaService aguaService;
    private final FacturaService facturaService;


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
    public ResponseEntity<DTOconsumoAcomuladoAgua> verconsumoAcomuladoAgua() {
        DTOconsumoAcomuladoAgua dtOconsumoAcomuladoAgua = aguaService.obtenerConsumoAcomulado();
        return ResponseEntity.ok(dtOconsumoAcomuladoAgua);
    }

    @Operation(
            summary = "ver las facturas de los servicios",
            description = "permite ver los detalles precios y consumo de las facturas"
            + "por su servicio"
    )
    @GetMapping("/usuario/servicio/{servicio}")
    public ResponseEntity<List<Factura>> obtenerFacturasPorServicio(
            @PathVariable("servicio") ServiciosEnum servicio) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuarios usuario = (Usuarios) auth.getPrincipal();

        List<Factura> facturas = facturaService.obtenerFacturasPorUsuarioYServicio(usuario.getIdUsuario(), servicio);
        return ResponseEntity.ok(facturas);
    }

}
