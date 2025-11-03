package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOEmailMiebro;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOPasswordMiebro;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOpersona;
import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Persona;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.PersonaCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.PersonaRepository;
import com.back_servicios.app_cosultas_servicios.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonaServiceimpl  implements PersonaService {


    private final PersonaRepository personaRepository;
    private final PersonaCreateMapper personaCreateMapper;
    private final HogarRepository hogarRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsuarioRepository usuarioRepository;

    @Override
    public DTOpersona crearMiembrosHogar(DTOpersona dtOpersona) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios Auth = (Usuarios) authentication.getPrincipal();

        if (Auth.getRole() != Role.ADMIN) {
            throw new ValidationException("solos los roles de admin pueden crear miembros del hogar");
        }

        Hogar hogar = hogarRepository.findById(dtOpersona.getHogar_id())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));


        Persona persona = personaCreateMapper.toEntity(dtOpersona);
        persona.setHogar(hogar);
        personaRepository.save(persona);
        return dtOpersona;
    }

    public void ComprobarEmailMiebro(DTOEmailMiebro dtoEmailMiebro) {

        if (dtoEmailMiebro.email() == null || dtoEmailMiebro.email().isEmpty()) {
            throw new ValidationException("El email es obligatorio");
        }

        if (personaRepository.findByEmail(dtoEmailMiebro.email()) == null) {
            throw new ValidationException("Email no existe ");

        }

        if(usuarioRepository.findByEmail(dtoEmailMiebro.email()) != null) {
            throw new ValidationException("No puedes usar el email del dueño de casa");
        }

}

public void  SetearPasswordMiebro(DTOPasswordMiebro dtoPasswordMiebro, Long id){


    String encryptedPassword = bCryptPasswordEncoder.encode(dtoPasswordMiebro.password());
    Persona persona = new Persona();
    persona.setPassword(encryptedPassword);
    personaRepository.save(persona);

}
}
