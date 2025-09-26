package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.HogarCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HogarServiceimpl implements HogarService {

    private final HogarCreateMapper hogarCreateMapper;
    private final HogarRepository hogarRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
public DTOhogar createHogar(DTOhogar dtohogar, Long idUsuario) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios Auth = (Usuarios) authentication.getPrincipal();

        if(Auth.getRole() != Role.ADMIN){
            throw new ValidationException("El usuario no tiene role administrador");
        }

        Usuarios usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado con id: " + idUsuario));


    Hogar hogar = hogarCreateMapper.toEntity(dtohogar);
        System.out.println("Usuario encontrado: " + usuario.getIdUsuario());
    hogar.setUsuario(usuario);
    hogarRepository.save(hogar);
  return dtohogar;

}

}
