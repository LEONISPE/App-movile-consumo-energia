package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.*;

public interface GasService {

    ConsumoDTODiarioGas calcularConsumoActual();
    ConsumoDTOacomuladoGas obtenerConsumoAcomulado();
}
