package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioAgua;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOconsumoAcomuladoAgua;

public interface AguaService {

    ConsumoDTODiarioAgua calcularConsumoActual();
    DTOconsumoAcomuladoAgua obtenerConsumoAcomulado();
}
