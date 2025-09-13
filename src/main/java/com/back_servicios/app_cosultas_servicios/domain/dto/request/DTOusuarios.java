package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOusuarios {


    private String nombres;
    private String apellidos;
    private String Email;
    private String telefono;
    private String contraseña;


}
