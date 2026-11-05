package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOfactura;
import com.back_servicios.app_cosultas_servicios.domain.entity.*;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.FacturaCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.FacturaRepository;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.PrecioTarifaRepository;
import com.back_servicios.app_cosultas_servicios.repository.ServiciosRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <h1>Implementación del Servicio de Gestión de Facturas</h1>
 * <p>
 * Servicio encargado de la creación explícita de comprobantes de facturación y de la
 * consulta histórica de recibos generados por usuario y tipo de servicio público.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */


@Service
@AllArgsConstructor
public class FacturaServiceimpl  implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final FacturaCreateMapper facturaCreateMapper;
    private final HogarRepository hogarRepository;
    private final ServiciosRepository serviciosRepository;
    private final PrecioTarifaRepository precioTarifaRepository;



    /**
     * Registra una nueva factura calculando el costo total a partir del consumo
     * registrado en el DTO y la tarifa por unidad vigente en el sistema.
     *
     * @param dtOfactura Objeto DTO con los datos de consumo, hogar y servicio.
     * @throws ValidationException Si el hogar, servicio o tarifa no existen.
     */
    @Override
    public void crearFactura(DTOfactura dtOfactura) {

        Hogar hogar = hogarRepository.findById(dtOfactura.getHogar_id())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));

        Servicios servicios = serviciosRepository.findById(dtOfactura.getServicio_id())
                .orElseThrow(() -> new ValidationException("Servicio no encontrado"));

        Tarifa_Servicio tarifa = precioTarifaRepository.findByServiciosId(servicios.getId())
                .orElseThrow(() -> new ValidationException("Tarifa no encontrada"));

        // Cálculo del costo total multiplicando las unidades consumidas por el precio unitario vigente
        BigDecimal costoTotal = dtOfactura.getConsumoTotal()
                .multiply(tarifa.getPreciounidad());

        Factura factura = facturaCreateMapper.toEntity(dtOfactura);
        factura.setHogar(hogar);
        factura.setServicios(servicios);
        factura.setCosto_total(costoTotal);
        facturaRepository.save(factura);
    }

    /**
     * Consulta el historial de facturas generadas asociadas a un usuario específico y a un tipo de servicio.
     *
     * @param usuarioId Identificador del usuario consultante.
     * @param servicio  Tipo de servicio público (AGUA, ENERGIA, GAS).
     * @return Lista de entidades Factura correspondientes al criterio.
     */
    @Override
    public List<Factura> obtenerFacturasPorUsuarioYServicio(Long usuarioId, ServiciosEnum servicio) {
        return facturaRepository.findByUsuarioAndServicio(usuarioId, servicio);
    }
}


