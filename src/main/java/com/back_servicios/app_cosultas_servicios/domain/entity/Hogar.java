package com.back_servicios.app_cosultas_servicios.domain.entity;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Ciudad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Hogar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHogar;
    private String calle;
    private String carrera;
    @Enumerated(EnumType.STRING)
    private Ciudad ciudad;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @OneToOne(mappedBy = "hogar")
    private Factura factura;

}
