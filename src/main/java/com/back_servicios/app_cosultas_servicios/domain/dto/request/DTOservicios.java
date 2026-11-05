package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Unidad;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTOservicios {

    @NotNull(message = "El tipo de servicio es obligatorio")
    private ServiciosEnum servicios;

    @NotNull(message = "La unidad de medida es obligatoria")
    private Unidad unidad;

}
