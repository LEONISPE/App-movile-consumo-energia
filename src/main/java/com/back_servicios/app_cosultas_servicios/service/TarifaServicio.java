package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOprecioTarifa;
import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Tarifa_Servicio;
import com.back_servicios.app_cosultas_servicios.repository.PrecioTarifaRepository;
import com.back_servicios.app_cosultas_servicios.repository.ServiciosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TarifaServicio {

    private PrecioTarifaRepository precioTarifaRepository;
    private ServiciosRepository serviciosRepository;


    public DTOprecioTarifa cargarPrecioTarifa(DTOprecioTarifa dtoprecioTarifa) {

        Servicios servicios =  serviciosRepository.findById(dtoprecioTarifa.getServicio_id())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Tarifa_Servicio tarifaServicio = new Tarifa_Servicio();
        tarifaServicio.setPrecioTarifa(dtoprecioTarifa.getPrecioTarifa());
        tarifaServicio.setServicios(servicios);
        precioTarifaRepository.save(tarifaServicio);
        return dtoprecioTarifa;

    }
}
