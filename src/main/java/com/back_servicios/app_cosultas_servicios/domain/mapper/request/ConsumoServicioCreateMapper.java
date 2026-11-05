package com.back_servicios.app_cosultas_servicios.domain.mapper.request;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOconsumoServicio;
import com.back_servicios.app_cosultas_servicios.domain.entity.Consumo_Servicio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsumoServicioCreateMapper {

    /** Convierte un DTO de creacion a la entidad Consumo Servicios. */
    Consumo_Servicio toEntity(DTOconsumoServicio dtOconsumoServicio);
}
