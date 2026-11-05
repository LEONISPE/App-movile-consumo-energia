package com.back_servicios.app_cosultas_servicios.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tarifa_servicio")
public class Tarifa_Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Long idTarifa;

    /** Precio unitario estipulado para el cálculo del costo del consumo del servicio. */
    private BigDecimal preciounidad;

    /** Fecha de inicio a partir de la cual entra a regir la tarifa vigente. */
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    /** Fecha de cierre o caducidad en la que deja de aplicar la tarifa. */
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

@ManyToOne
@JoinColumn(name = "servicio_id")
@JsonIgnore
private Servicios servicios;

}
