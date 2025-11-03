package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOUpdatePassword {

    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
            message = "Contraseña deber tener 8 caracteres, con al menos un digito y una mayúscula"
    )
    String  password;
}
