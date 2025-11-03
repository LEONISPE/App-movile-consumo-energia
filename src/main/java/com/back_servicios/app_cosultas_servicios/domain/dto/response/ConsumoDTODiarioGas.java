package com.back_servicios.app_cosultas_servicios.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumoDTODiarioGas {
    private BigDecimal consumoActualHoy;
    private BigDecimal costoActualHoy;
    private BigDecimal consumoHora;
}
