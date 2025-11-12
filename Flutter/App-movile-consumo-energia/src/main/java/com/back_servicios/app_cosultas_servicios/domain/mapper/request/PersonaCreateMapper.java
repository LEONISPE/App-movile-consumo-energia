package com.back_servicios.app_cosultas_servicios.domain.mapper.request;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOpersona;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonaCreateMapper {

    Persona toEntity(DTOpersona dtOpersona);
}
