package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOUpdateUsuario {

    private String nombres;
    private String apellidos;
    private String telefono;
}
