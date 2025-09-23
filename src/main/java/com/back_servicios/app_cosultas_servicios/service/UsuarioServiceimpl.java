package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOadmin;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOusuarios;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOusuariosResponse;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.UsuarioCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UsuarioServiceimpl  implements UsuarioService {

    private final UsuarioCreateMapper usuarioCreateMapper;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public DTOadmin crearAdmin(DTOadmin dtOadmin){
        if(dtOadmin.getEmail() == null || dtOadmin.getEmail().isEmpty()){
            throw new ValidationException("El email es obligatorio");
        }
        if(usuarioRepository.findByEmail(dtOadmin.getEmail())  !=null){
            throw new ValidationException("El email ya existe");
        }
        String encryptedPassword = bCryptPasswordEncoder.encode(dtOadmin.getContraseña());
        Usuarios usuarios = new Usuarios();
        usuarios.setEmail(dtOadmin.getEmail());
        usuarios.setContraseña(encryptedPassword);
        usuarios.setRole(Role.ADMIN);
        usuarioRepository.save(usuarios);
        return dtOadmin;
    }


    @Override
public DTOusuarios crearUsuarios(DTOusuarios dtOusuarios){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios Auth = (Usuarios) authentication.getPrincipal();

   if(Auth.getRole()!= Role.ADMIN){
       throw new ValidationException("solos los roles de admin pueden crear user nuevos");
   }

        if (dtOusuarios.email() == null) {
            throw new ValidationException("Email no puede estar vacio");
        }
        // Check if email already exists
        if (usuarioRepository.findByEmail(dtOusuarios.email()) != null) {
            throw new ValidationException("Email ya existe");
        }

        // Encriptar la contraseña
        String encryptedPassword = bCryptPasswordEncoder.encode(dtOusuarios.contraseña());

    Usuarios usuarios =  usuarioCreateMapper.toEntity(dtOusuarios);
    usuarios.setContraseña(encryptedPassword);
    usuarioRepository.save(usuarios);
  return dtOusuarios;
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
