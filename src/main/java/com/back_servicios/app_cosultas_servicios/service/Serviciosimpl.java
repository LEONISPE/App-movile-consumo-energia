package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOservicios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.ServiciosCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.ServiciosRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Serviciosimpl  implements ServiciosService{

    private final ServiciosRepository serviciosrepository;
    private final ServiciosCreateMapper serviciosmapper;

@Override
    public DTOservicios crearServicios(DTOservicios dtoservicios) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuarios Auth = (Usuarios) authentication.getPrincipal();

    if(Auth.getRole() != Role.ADMIN){
        throw new ValidationException("El usuario no tiene role administrador");
    }
        Servicios servicios = serviciosmapper.toEntity(dtoservicios);
        serviciosrepository.save(servicios);
        return dtoservicios;
    }

}
