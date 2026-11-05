package com.back_servicios.app_cosultas_servicios.domain.mapper.request;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOservicios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiciosCreateMapper {
    /** Convierte un DTO de creacion a la entidad Servicios. */
    Servicios toEntity(DTOservicios dtoservicios);
}
