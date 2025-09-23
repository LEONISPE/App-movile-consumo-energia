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

    public DTOprecioTarifa crearTarifaAgua(DTOprecioTarifa dtoprecioTarifa) {

        // buscamos el servicio "Agua" (supongamos que ya existe en la BD con id = 1)
        Servicios servicio = serviciosRepository.findById(dtoprecioTarifa.getServicio_id())
                .orElseThrow(() -> new RuntimeException("Servicio Agua no encontrado"));

        // creamos una nueva tarifa
        Tarifa_Servicio nuevaTarifa = new Tarifa_Servicio();
        nuevaTarifa.setPreciounidad(dtoprecioTarifa.getPreciounidad());
        nuevaTarifa.setFechaInicio(dtoprecioTarifa.getFechaInicio());
        nuevaTarifa.setFechaFin(dtoprecioTarifa.getFechaFin());
        nuevaTarifa.setServicios(servicio);

        // guardamos en la BD
        precioTarifaRepository.save(nuevaTarifa);

        return dtoprecioTarifa;
    }






    public DTOprecioTarifa modificarPrecioTarifaAgua(Long idTarifa, DTOprecioTarifa dtoprecioTarifa) {

        Servicios servicios = serviciosRepository.findById(dtoprecioTarifa.getServicio_id())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Tarifa_Servicio tarifaServicio = precioTarifaRepository.findById(idTarifa)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada"));

        tarifaServicio.setPreciounidad(dtoprecioTarifa.getPreciounidad());
        tarifaServicio.setFechaInicio(dtoprecioTarifa.getFechaInicio());
        tarifaServicio.setFechaFin(dtoprecioTarifa.getFechaFin());
        tarifaServicio.setServicios(servicios);

        precioTarifaRepository.save(tarifaServicio);

        return dtoprecioTarifa;
    }

}