package com.back_servicios.app_cosultas_servicios.domain.entity;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Ciudad;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Agua;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Energia;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Gas;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Hogar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hogar")
    private Long idHogar;
    private String calle;
    private String carrera;
    @Enumerated(EnumType.STRING)
    private Ciudad ciudad;

    @Enumerated(EnumType.STRING)
    private Estrato_Agua estratoAgua;

    @Enumerated(EnumType.STRING)
    @Column(name = "estrato_energia")
    private Estrato_Energia estratoEnergia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estrato_gas")
    private Estrato_Gas estratoGas;

    @Column(name = "numero_integrantes_original")
    private int numeroIntegrantesOriginal;

    @Column(name = "suma_factores_originales")
    private double sumaFactoresOriginales;



    @OneToOne
    @JsonManagedReference
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @OneToMany(mappedBy = "hogar", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Factura> facturas;

    @OneToMany(mappedBy = "hogar")
    private List<Consumo_Servicio> consumo_servicio;

    @OneToMany(mappedBy = "hogar", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Persona> personas = new ArrayList<>();

}
