package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOmiebrosHogar;


import java.util.List;

public interface HogarService {

    DTOhogar createHogar(DTOhogar dtohogar, Long idUsuario);
    List<DTOmiebrosHogar> obtenerMiembrosDelHogar(Long id_hogar);
}
