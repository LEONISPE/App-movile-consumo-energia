package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOservicios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.ServiciosCreateMapper;
import com.back_servicios.app_cosultas_servicios.repository.ServiciosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Serviciosimpl  implements ServiciosService{

    private final ServiciosRepository serviciosrepository;
    private final ServiciosCreateMapper serviciosmapper;

@Override
    public DTOservicios crearServicios(DTOservicios dtoservicios) {
        Servicios servicios = serviciosmapper.toEntity(dtoservicios);
        serviciosrepository.save(servicios);
        return dtoservicios;
    }

}
