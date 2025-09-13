package com.back_servicios.app_cosultas_servicios.domain.entity;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Unidad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Servicios {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ServiciosEnum  servicios;
    private Unidad unidad;

    @OneToOne(mappedBy = "servicios")
    private Tarifa_Servicio tarifa_servicio;

    @OneToOne(mappedBy = "servicios")
    private Factura factura;

    @OneToOne(mappedBy = "servicios")
    private Consumo_Servicio consumo_servicio;

}
