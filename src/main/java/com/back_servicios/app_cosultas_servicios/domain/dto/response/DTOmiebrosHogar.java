package com.back_servicios.app_cosultas_servicios.domain.dto.response;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOmiebrosHogar {
    private Long id;
    private String nombre;
    private String email;
    private Role role;
}
