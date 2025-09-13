package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOusuarios;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOusuariosResponse;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.UsuarioCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UsuarioServiceimpl  implements UsuarioService {

    private final UsuarioCreateMapper usuarioCreateMapper;
    private final UsuarioRepository usuarioRepository;

    @Override
public void crearUsuarios(DTOusuarios dtOusuarios){

    Usuarios usuarios =  usuarioCreateMapper.toEntity(dtOusuarios);
    usuarioRepository.save(usuarios);

}

@Override
public void updateUsuarios(Long id , DTOusuarios dtOusuarios){
   if(!usuarioRepository.findById(id).isPresent()){
  throw new ValidationException("Usuario no encontrado");
   }
Usuarios usuarios = usuarioCreateMapper.toEntity(dtOusuarios);
   usuarioRepository.save(usuarios);

}

@Override
public DTOusuariosResponse getUsuarios(Long id){

return usuarioRepository.findById(id)
        .map(usuarios -> new DTOusuariosResponse(
                usuarios.getNombres(),
                usuarios.getApellidos(),
                usuarios.getEmail(),
                usuarios.getTelefono()
        )).orElseThrow(() -> new ValidationException("Usuario con ID " + id + " no encontrado"));
}


}
