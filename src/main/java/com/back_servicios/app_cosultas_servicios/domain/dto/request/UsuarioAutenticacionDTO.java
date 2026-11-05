package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioAutenticacionDTO(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe proporcionar un formato de email válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
