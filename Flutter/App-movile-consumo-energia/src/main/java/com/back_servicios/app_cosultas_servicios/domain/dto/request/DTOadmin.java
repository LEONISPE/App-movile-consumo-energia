package com.back_servicios.app_cosultas_servicios.domain.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTOadmin {

    @NotBlank
    @Email
    private String Email;
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
            message = "Contraseña deber tener 8 caracteres, con al menos un digito y una mayúscula"
    )
    @NotBlank
    private String contraseña;
}
