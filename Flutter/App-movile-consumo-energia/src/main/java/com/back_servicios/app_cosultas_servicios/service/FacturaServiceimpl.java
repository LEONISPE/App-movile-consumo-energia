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

@Service
@AllArgsConstructor
public class FacturaServiceimpl  implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final FacturaCreateMapper facturaCreateMapper;
    private final HogarRepository hogarRepository;
    private final ServiciosRepository serviciosRepository;
    private final PrecioTarifaRepository precioTarifaRepository;


    @Override
    public void crearFactura(DTOfactura dtOfactura) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios Auth = (Usuarios) authentication.getPrincipal();

        if(Auth.getRole() != Role.ADMIN){
            throw new ValidationException("El usuario no tiene role administrador");
        }

        Hogar hogar = hogarRepository.findById(dtOfactura.getHogar_id())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));

        Servicios servicios = serviciosRepository.findById(dtOfactura.getServicio_id())
                .orElseThrow(() -> new ValidationException("Servicio no encontrado"));

        Tarifa_Servicio tarifa = precioTarifaRepository.findByServiciosId(servicios.getId())
                .orElseThrow(() -> new ValidationException("Tarifa no encontrada"));

        BigDecimal costoTotal = dtOfactura.getConsumoTotal()
                .multiply(tarifa.getPreciounidad());

        Factura factura = facturaCreateMapper.toEntity(dtOfactura);
        factura.setHogar(hogar);
        factura.setServicios(servicios);
        factura.setCosto_total(costoTotal);
        facturaRepository.save(factura);
    }

    @Override
    public List<Factura> obtenerFacturasPorUsuarioYServicio(Long usuarioId, ServiciosEnum servicio) {
        return facturaRepository.findByUsuarioAndServicio(usuarioId, servicio);
    }
}


