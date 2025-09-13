package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Ciudad;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTOhogar {

    private String calle;
    private String carrera;
    private Ciudad ciudad;
}
