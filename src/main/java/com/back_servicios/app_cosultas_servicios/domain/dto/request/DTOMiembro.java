package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Categoria;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOMiembro {

    private String nombres;
     private String apellidos;
    private int edad;
    private String telefono;
    private Categoria categoria;
    private Role role;
    private Long hogar_id;
}
