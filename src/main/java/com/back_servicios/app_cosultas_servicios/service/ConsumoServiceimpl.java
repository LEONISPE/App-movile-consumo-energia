package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOconsumoServicio;
import com.back_servicios.app_cosultas_servicios.domain.entity.Consumo_Servicio;
import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.ConsumoServicioCreateMapper;
import com.back_servicios.app_cosultas_servicios.repository.ConsumoServicioRepository;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.ServiciosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsumoServiceimpl implements ConsumoServicio {

    private final ConsumoServicioRepository consumoServicioRepository;
    private final HogarRepository hogarRepository;
    private final ServiciosRepository serviciosRepository;
    private final ConsumoServicioCreateMapper consumoServicioCreateMapper;


    @Override
    public DTOconsumoServicio crearDTOconsumoServicio(DTOconsumoServicio dtoconsumoServicio) {
        Hogar hogar = hogarRepository.findById(dtoconsumoServicio.getHogar_id())
                .orElseThrow(() -> new RuntimeException("Hogar no encontrado"));

        Servicios servicios = serviciosRepository.findById(dtoconsumoServicio.getServicio_id())
                .orElseThrow(() -> new RuntimeException("Servicios no encontrado"));

        Consumo_Servicio consumoServicio = new Consumo_Servicio();
        consumoServicio.setHogar(hogar);
        consumoServicio.setServicios(servicios);

        consumoServicio = consumoServicioCreateMapper.toEntity(dtoconsumoServicio);
    consumoServicioRepository.save(consumoServicio);
    return dtoconsumoServicio;
    }
}

