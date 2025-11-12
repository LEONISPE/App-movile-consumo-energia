package com.back_servicios.app_cosultas_servicios.domain.dto.request;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Categoria;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
public record DTOusuarios(

@NotBlank(message = "el nombre no puede estar en blanco")
String nombres,
int edad,
@NotBlank(message = "el apellido no puede estar nulo")
String apellidos,
Role role,
Categoria categoria,
 @Email
 @NotBlank(message = "Email no puede estar vacío")
String email,

String telefono,
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
            message = "Contraseña deber tener 8 caracteres, con al menos un digito y una mayúscula"
    )
     String contraseña

)
        {
}
