package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioAgua;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOconsumoAcomulado;

public interface AguaService {

    ConsumoDTODiarioAgua calcularConsumoActual();
    DTOconsumoAcomulado obtenerConsumoAcomulado();
}
