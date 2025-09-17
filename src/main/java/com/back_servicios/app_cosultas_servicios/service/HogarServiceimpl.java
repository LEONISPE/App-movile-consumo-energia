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

    Usuarios usuarios = usuarioRepository.findById(idUsuario).get();
    if (usuarios == null) {
        throw new ValidationException("usuario no encontrado");
    }

    Hogar hogar = hogarCreateMapper.toEntity(dtohogar);
    hogar.setUsuario(usuarios);
    hogarRepository.save(hogar);
  return dtohogar;

}

}
