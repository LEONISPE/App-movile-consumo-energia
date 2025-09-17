package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class DTOprecioTarifa {
    private BigDecimal precioTarifa;
     private Long servicio_id;

}
