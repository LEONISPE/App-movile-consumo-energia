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
/**
 * <h1>Implementación del servicio de Catálogo de Servicios</h1>
 * <p>
 * Clase ligera de servicio encargada de gestionar las operaciones básicas para la creación
 * y administración de los tipos de servicios públicos disponibles en la plataforma (ej. Agua, Luz, Gas).
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */




@Service
@AllArgsConstructor
public class Serviciosimpl  implements ServiciosService{

    private final ServiciosRepository serviciosrepository;
    private final ServiciosCreateMapper serviciosmapper;

    /**
     * Registra un nuevo servicio público en la base de datos a partir del DTO recibido,
     * utilizando un componente Mapper para la conversión a entidad de dominio.
     *
     * @param dtoservicios Objeto DTO que contiene la información del servicio a crear.
     * @return El DTO del servicio creado exitosamente.
     */
@Override
    public DTOservicios crearServicios(DTOservicios dtoservicios) {
        Servicios servicios = serviciosmapper.toEntity(dtoservicios);
        serviciosrepository.save(servicios);
        return dtoservicios;
    }

}
