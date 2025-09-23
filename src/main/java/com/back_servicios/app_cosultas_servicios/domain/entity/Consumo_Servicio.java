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
public class Consumo_Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsumo;

    @OneToOne
    @JoinColumn(name = "hogar_id")
    private Hogar hogar;

    @OneToOne
    @JoinColumn(name = "servicio_id")
    private Servicios servicios;

     private LocalDateTime fecha;
     private double consumo;
     private BigDecimal costo;

}
