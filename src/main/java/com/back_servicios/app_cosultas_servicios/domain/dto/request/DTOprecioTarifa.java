package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DTOprecioTarifa {

    @NotNull(message = "El precio por unidad es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private BigDecimal preciounidad;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin; // Puede ser null si está vigente

    @NotNull(message = "El ID del servicio es obligatorio")
    private Long servicio_id;

}
