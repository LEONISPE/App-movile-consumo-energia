package com.back_servicios.app_cosultas_servicios.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa_Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarifa;

    private BigDecimal preciounidad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

@ManyToOne
@JoinColumn(name = "servicio_id")
private Servicios servicios;

}
