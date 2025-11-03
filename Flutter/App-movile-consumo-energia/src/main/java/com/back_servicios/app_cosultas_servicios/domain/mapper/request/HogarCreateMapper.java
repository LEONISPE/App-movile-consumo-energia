package com.back_servicios.app_cosultas_servicios.domain.mapper.request;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HogarCreateMapper {

    Hogar toEntity(DTOhogar dtohogar);
}
