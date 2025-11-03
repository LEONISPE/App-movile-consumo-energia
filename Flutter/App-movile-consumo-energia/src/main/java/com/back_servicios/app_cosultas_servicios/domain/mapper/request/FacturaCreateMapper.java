package com.back_servicios.app_cosultas_servicios.domain.mapper.request;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOfactura;
import com.back_servicios.app_cosultas_servicios.domain.entity.Factura;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacturaCreateMapper {

    Factura toEntity(DTOfactura dtOfactura);
}
