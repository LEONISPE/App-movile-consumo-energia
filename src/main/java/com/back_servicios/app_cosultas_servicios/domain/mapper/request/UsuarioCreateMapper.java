package com.back_servicios.app_cosultas_servicios.domain.mapper.request;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOMiembro;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOUpdateUsuario;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOusuarios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import org.mapstruct.Mapper;

/**
 * Interfaz de mapeo basada en MapStruct (componentModel = "spring").
 * Automatiza la conversión de objetos DTO (Data Transfer Object) hacia entidades de persistencia (Usuarios),
 * evitando el acoplamiento manual de propiedades en la capa de servicios y mejorando la legibilidad del código.
 */
@Mapper(componentModel = "spring")
public interface UsuarioCreateMapper {

    /** Convierte un DTO de actualización a la entidad Usuarios. */
    Usuarios toEntity(DTOUpdateUsuario dtoUpdateUsuario);
    /** Convierte un DTO de creación general de usuario a la entidad Usuarios. */
    Usuarios toEntity(DTOusuarios dtousuarios);
    /** Convierte un DTO de miembro de hogar a la entidad Usuarios. */
    Usuarios toEntity(DTOMiembro dtoMiembro);
}
