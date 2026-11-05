package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Ciudad;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Agua;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Energia;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Gas;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOhogar {
    @NotBlank(message = "La calle es obligatoria")
    private String calle;

    @NotBlank(message = "La carrera es obligatoria")
    private String carrera;

    @NotNull(message = "La ciudad es obligatoria")
    private Ciudad ciudad;

    private Estrato_Gas estratoGas;
    private Estrato_Agua estratoAgua;

    @Min(value = 1, message	= "Debe haber al menos 1 integrante en el hogar")
    private int numeroIntegrantesOriginal;

    @Min(value = 1, message	= "Debe haber al menos 1 suma de factores en el hogar")
    private double sumaFactoresOriginales;

    @NotNull(message = "El estrato es obligatorio")
    private Estrato_Energia estratoEnergia;

}
