package com.back_servicios.app_cosultas_servicios.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "factura")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factura_id")
    private Long facturaId;

    /** Fecha exacta en la que inicia el periodo de cobro y medición del recibo. */
    @Column(name = "fecha_periodo_inicio")
    private LocalDate fecha_periodoInicio;

    /** Fecha exacta en la que finaliza el ciclo de facturación mensual. */
    @Column(name = "fechaPeriodoFin")
    private LocalDate fechaPeriodoFin;

    /** Volumen total de consumo registrado durante todo el periodo del recibo. */
    @Column(name = "consumo_total")
    private BigDecimal consumoTotal;

    /** Costo monetario total calculado para la factura en base al consumo y tarifas vigentes. */
    private BigDecimal costo_total;

    @ManyToOne
    @JoinColumn(name = "hogar_id", nullable = false)
    @JsonIgnore
    private Hogar hogar;

    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    @JsonBackReference
    private Servicios servicios;

}
