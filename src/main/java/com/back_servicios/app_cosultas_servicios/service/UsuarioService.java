package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.*;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOusuariosResponse;
import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;

public interface UsuarioService {


    DTOadmin crearAdmin(DTOadmin dtOadmin);
    DTOusuarios crearUsuarios(DTOusuarios dtOusuarios);
    void updateUsuarios(DTOUpdateUsuario dtoUpdateUsuario);
    DTOusuariosResponse getUsuarios();
    void AutorizarMiembro(Long id);
    void SetearEmailMiebro(DTOEmailMiebro dtoEmailMiebro, Long id);
    void  actualizarPasword(DTOUpdatePassword dtoUpdatePassword);
}
