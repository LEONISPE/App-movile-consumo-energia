package com.back_servicios.app_cosultas_servicios.domain.dto.request;


import jakarta.validation.constraints.Pattern;

public record DTOPasswordMiebro(
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
                message = "Contraseña deber tener 8 caracteres, con al menos un digito y una mayúscula"
        )
        String  password
) {
}
