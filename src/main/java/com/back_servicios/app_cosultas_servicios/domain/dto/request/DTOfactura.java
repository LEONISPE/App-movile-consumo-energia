package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull(message = "La fecha de inicio del periodo es obligatoria")
    private LocalDate fecha_periodoInicio;

    @NotNull(message = "La fecha de fin del periodo es obligatoria")
    private LocalDate fechaPeriodoFin;

    @NotNull(message = "El consumo total es obligatorio")
    @Positive(message = "El consumo total debe ser mayor a cero")
    private BigDecimal consumoTotal;

    @NotNull(message = "El ID del hogar es obligatorio")
    private Long hogar_id;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Long servicio_id;
}
