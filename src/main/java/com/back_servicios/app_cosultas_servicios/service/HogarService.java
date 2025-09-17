package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;

public interface HogarService {

    DTOhogar createHogar(DTOhogar dtohogar, Long idUsuario);
}
