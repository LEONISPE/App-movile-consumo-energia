package com.back_servicios.app_cosultas_servicios.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ConsumoDTODiarioAgua {

    private BigDecimal consumoActualHoy;
    private BigDecimal costoActualHoy;
    private BigDecimal consumoHora;
}
