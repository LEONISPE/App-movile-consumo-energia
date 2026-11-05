package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Categoria;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOMiembro {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private int edad;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotNull(message = "La categoría es obligatoria")
    private Categoria categoria;

    @NotNull(message = "El rol es obligatorio")
    private Role role;

    @NotNull(message = "El ID del hogar es obligatorio")
    private Long hogar_id;
}
