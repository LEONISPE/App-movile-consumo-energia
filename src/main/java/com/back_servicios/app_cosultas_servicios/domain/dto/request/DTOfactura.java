package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOfactura {

    private LocalDate fecha_periodoInicio;
    private LocalDate fechaPeriodoFin;
    private BigDecimal consumoTotal;
    private Long hogar_id;
    private Long servicio_id;
}
