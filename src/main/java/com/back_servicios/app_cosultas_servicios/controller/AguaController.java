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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * <h1>Controlador REST para el Monitoreo de Agua y Facturación</h1>
 * <p>
 * Expone los endpoints para consultar el consumo diario, acumulado de agua potable
 * y el historial de facturas detalladas por servicio del usuario autenticado.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */
@Tag(name = "Monitoreo de Agua y Facturas", description = "Endpoints para consumo de agua y consulta de facturas históricas")

@SecurityRequirement(name = "bearer-key")
@RestController
@AllArgsConstructor
public class AguaController {

    private final AguaService aguaService;
    private final FacturaService facturaService;


    /**
     * Consulta el consumo de agua estimado en tiempo real para el día actual.
     *
     * @return DTO con el consumo diario de agua y costo base.
     */
    @Operation(
            summary = "ver consumo de agua  en tiempo real",
            description = "Permite ver el consumo del agua en el dia ."
    )
    @GetMapping("/agua/consumo")
    public ResponseEntity<ConsumoDTODiarioAgua> verconsumoAgua() {
        ConsumoDTODiarioAgua consumoDTO = aguaService.calcularConsumoActual();
        return ResponseEntity.ok(consumoDTO);

    }

    /**
     * Consulta el consumo acumulado de agua desde el inicio del período de facturación.
     *
     * @return DTO con el consumo acumulado total y costo monetario.
     */

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

    /**
     * Consulta el historial de facturas del usuario autenticado filtradas por un servicio específico.
     *
     * @param servicio {@code @PathVariable} Tipo de servicio enumerado (AGUA, ENERGIA, GAS).
     * @return Lista de entidades Factura asociadas.
     */
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
