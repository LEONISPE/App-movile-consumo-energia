package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOconsumoServicio {

    private LocalDateTime fecha;
    private double consumo;
    private BigDecimal costo;
    private Long hogar_id;
    private Long servicio_id;

}
