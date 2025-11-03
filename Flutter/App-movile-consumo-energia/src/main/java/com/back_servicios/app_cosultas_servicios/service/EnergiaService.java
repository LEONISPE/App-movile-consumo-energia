package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioEnergia;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOConsumoAcomuladoEnergia;

public interface EnergiaService {

    ConsumoDTODiarioEnergia calcularConsumoDiarioEnergia();
    DTOConsumoAcomuladoEnergia ObtenerConsumoAcomuladoEnergia();
}
