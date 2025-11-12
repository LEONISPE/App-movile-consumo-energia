package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Ciudad;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Agua;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Energia;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Gas;
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
    private Estrato_Gas estratoGas;
    private Estrato_Agua estratoAgua;
    private int numeroIntegrantesOriginal;
    private double sumaFactoresOriginales;
    private Estrato_Energia estratoEnergia;

}
