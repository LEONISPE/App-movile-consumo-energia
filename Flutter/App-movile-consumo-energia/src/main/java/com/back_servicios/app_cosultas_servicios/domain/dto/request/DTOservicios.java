package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Unidad;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTOservicios {

    private ServiciosEnum servicios;
    private Unidad unidad;

}
