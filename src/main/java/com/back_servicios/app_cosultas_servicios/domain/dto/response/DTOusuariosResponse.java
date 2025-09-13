package com.back_servicios.app_cosultas_servicios.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOusuariosResponse {


    private String nombres;
    private String apellidos;
    private String Email;
    private String telefono;
}
