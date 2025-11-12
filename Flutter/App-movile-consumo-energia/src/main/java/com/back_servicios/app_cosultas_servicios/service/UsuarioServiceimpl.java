package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.*;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOusuariosResponse;
import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.UsuarioCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
    private final PersonaRepository personaRepository;
    private final HogarRepository  hogarRepository;


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
        usuarios.setPassword(encryptedPassword);
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

        if (usuarioRepository.findByEmail(dtOusuarios.email()) != null) {
            throw new ValidationException("Email ya existe");
        }


        String encryptedPassword = bCryptPasswordEncoder.encode(dtOusuarios.contraseña());

    Usuarios usuarios =  usuarioCreateMapper.toEntity(dtOusuarios);
    usuarios.setPassword(encryptedPassword);
    usuarioRepository.save(usuarios);
  return dtOusuarios;
}

    @Override
    public void updateUsuarios(DTOUpdateUsuario dtoUpdateUsuario) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios auth = (Usuarios) authentication.getPrincipal();

        if (auth.getRole() == Role.DUEÑO) {
            Usuarios usuario = usuarioRepository.findById(auth.getIdUsuario())
                    .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

            usuario.setNombres(dtoUpdateUsuario.getNombres());
            usuario.setApellidos(dtoUpdateUsuario.getApellidos());
            usuario.setTelefono(dtoUpdateUsuario.getTelefono());

            usuarioRepository.save(usuario);

        } else if (auth.getRole() == Role.MIEMBRO) {
            Persona persona = personaRepository.findById(auth.getIdUsuario())
                    .orElseThrow(() -> new ValidationException("Persona no encontrada"));

            persona.setNombre(dtoUpdateUsuario.getNombres());
            persona.setApellido(dtoUpdateUsuario.getApellidos());
            persona.setTelefono(dtoUpdateUsuario.getTelefono());

            personaRepository.save(persona);
        }
    }

@Override
public DTOusuariosResponse getUsuarios(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuarios Auth = (Usuarios) authentication.getPrincipal();

return usuarioRepository.findById(Auth.getIdUsuario())
        .map(usuarios -> new DTOusuariosResponse(
                usuarios.getNombres(),
                usuarios.getApellidos(),
                usuarios.getEmail(),
                usuarios.getTelefono()
        )).orElseThrow(() -> new ValidationException("Usuario no encontrado"));
}

@Transactional
public void AutorizarMiembro(Long id){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuarios Auth = (Usuarios) authentication.getPrincipal();

    if(Auth.getRole()!= Role.DUEÑO){
        throw new ValidationException("Solo lo dueños de los hogares pueden autorizar");
    }
    Persona miembro = personaRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Miembro no encontrado."));

    Hogar hogarDelDueno = Auth.getHogar();
    if (hogarDelDueno == null) {
        throw new ValidationException("El dueño no tiene un hogar registrado.");
    }
    if (!miembro.getHogar().getIdHogar().equals(hogarDelDueno.getIdHogar())) {
        throw new ValidationException("El miembro no pertenece al mismo hogar del dueño.");
    }

    miembro.setAutorizado(true);
}
@Override
public void SetearEmailMiebro(DTOEmailMiebro dtoEmailMiebro,Long id){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuarios Auth = (Usuarios) authentication.getPrincipal();

    if(Auth.getRole()!= Role.DUEÑO){
        throw new ValidationException("Solo lo dueños de los hogares pueden hacer esta accion");
    }

    if(dtoEmailMiebro.email() == null || dtoEmailMiebro.email().isEmpty()){
        throw new ValidationException("El email es obligatorio");
    }

    if (usuarioRepository.findByEmail(dtoEmailMiebro.email()) != null) {
        throw new ValidationException("Email ya existe");
    }

    Persona miembro = personaRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Miembro no encontrado."));

    if(miembro.getAutorizado() == false){
        throw new ValidationException("el miembro no esta autorizado");
    }

    Hogar hogarDelDueno = Auth.getHogar();
    if (hogarDelDueno == null) {
        throw new ValidationException("El dueño no tiene un hogar registrado.");
    }
    if (!miembro.getHogar().getIdHogar().equals(hogarDelDueno.getIdHogar())) {
        throw new ValidationException("El miembro no pertenece al mismo hogar del dueño.");
    }

    Persona persona = new Persona();
    persona.setEmail(dtoEmailMiebro.email());
    personaRepository.save(persona);
}

public void  actualizarPasword(DTOUpdatePassword dtoUpdatePassword){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuarios Auth = (Usuarios) authentication.getPrincipal();

    if(usuarioRepository.findByEmail(Auth.getEmail())== null || personaRepository.findByEmail(Auth.getEmail()) == null){
        throw new ValidationException("El usuario no existe");
    }
    String encryptedPassword = bCryptPasswordEncoder.encode(dtoUpdatePassword.getPassword());

    if(Auth.getRole() == Role.DUEÑO){
        Usuarios usuarios = new Usuarios();
        usuarios.setPassword(encryptedPassword);
        usuarioRepository.save(usuarios);

    }else if(Auth.getRole() == Role.MIEMBRO){
        Persona persona = new Persona();
        persona.setPassword(encryptedPassword);
        personaRepository.save(persona);
    }


}

}
