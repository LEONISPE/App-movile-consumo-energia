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
    @Column(name = "fecha_periodo_inicio")
    private LocalDate fecha_periodoInicio;

    @Column(name = "fechaPeriodoFin")
    private LocalDate fechaPeriodoFin;

    @Column(name = "consumo_total")
    private BigDecimal consumoTotal;
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
