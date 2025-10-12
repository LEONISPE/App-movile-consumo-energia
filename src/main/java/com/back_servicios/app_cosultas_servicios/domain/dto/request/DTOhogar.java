package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Ciudad;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOhogar {

    private String calle;
    private String carrera;
    private Ciudad ciudad;
    private Estrato estrato;
    private int numeroIntegrantesOriginal;
    private double sumaFactoresOriginales;

}
