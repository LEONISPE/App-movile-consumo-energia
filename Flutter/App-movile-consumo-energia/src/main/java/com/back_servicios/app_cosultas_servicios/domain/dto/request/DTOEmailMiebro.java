package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public record DTOEmailMiebro(
        @Email
        @NotBlank(message = "Email no puede estar vacío")
        String email
) {

}
