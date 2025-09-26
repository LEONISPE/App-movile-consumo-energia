package com.back_servicios.app_cosultas_servicios.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facturaId;

    private LocalDate fecha_periodoInicio;
    private LocalDate fecha_periodoFin;
    private BigDecimal consumoTotal;
    private BigDecimal costo_total;

    @ManyToOne
    @JoinColumn(name = "hogar_id", nullable = false)
    private Hogar hogar;

    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicios servicios;

}
