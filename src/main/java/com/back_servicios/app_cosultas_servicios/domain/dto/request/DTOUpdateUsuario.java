package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOUpdateUsuario {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
}
