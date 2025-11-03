package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DTOprecioTarifa {

    private BigDecimal preciounidad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
     private Long servicio_id;

}
