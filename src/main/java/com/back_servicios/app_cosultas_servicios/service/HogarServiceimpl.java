package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.HogarCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HogarServiceimpl implements HogarService {

    private final HogarCreateMapper hogarCreateMapper;
    private final HogarRepository hogarRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
public DTOhogar createHogar(DTOhogar dtohogar, Long idUsuario) {

        Usuarios usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idUsuario));


        if (dtohogar.getCiudad() == null) {
            throw new ValidationException("La ciudad es obligatoria");
        }

    Hogar hogar = hogarCreateMapper.toEntity(dtohogar);
        System.out.println("Usuario encontrado: " + usuario.getIdUsuario());
    hogar.setUsuario(usuario);
    hogarRepository.save(hogar);
  return dtohogar;

}

}
