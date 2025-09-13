package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOusuarios;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOusuariosResponse;

public interface UsuarioService {


    void crearUsuarios(DTOusuarios dtOusuarios);
    void updateUsuarios(  Long id , DTOusuarios dtOusuarios);
    DTOusuariosResponse getUsuarios(Long id);
}
