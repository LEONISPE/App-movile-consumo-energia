package com.back_servicios.app_cosultas_servicios.domain.mapper.request;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOusuarios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioCreateMapper {

    Usuarios toEntity(DTOusuarios dtousuarios);

}
