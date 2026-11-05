package com.back_servicios.app_cosultas_servicios.domain.entity;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Unidad;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "servicios")
public class Servicios {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Enumerated(EnumType.STRING)
   private ServiciosEnum  servicios;

    /**
     * Unidad de medida métrica asignada al servicio público.
     * Ejemplo: Agua y Gas se miden en metros cúbicos ($m^3$), mientras que la Luz/Energía se mide en kilovatios (kWh).
     */
   @Enumerated(EnumType.STRING)
    private Unidad unidad;

    @OneToMany(mappedBy = "servicios", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Tarifa_Servicio> tarifas = new ArrayList<>();

    @OneToMany(mappedBy = "servicios")
    @JsonManagedReference
    private List<Factura> facturas;

    @OneToMany(mappedBy = "servicios")
    private List<Consumo_Servicio> consumo_servicio;

}
