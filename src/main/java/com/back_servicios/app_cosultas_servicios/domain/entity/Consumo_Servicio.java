package com.back_servicios.app_cosultas_servicios.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumo_servicio")
public class Consumo_Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consumo")
    private Long idConsumo;

    @ManyToOne
    @JoinColumn(name = "hogar_id", nullable = false)
    private Hogar hogar;

    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicios servicios;

    private LocalDate fecha;
    private BigDecimal consumo;
    private BigDecimal costo;

}
