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


/**
        * <h1>Implementación del servicio de gestión de usuarios</h1>
        * <p>
 * Clase de servicio encargada de centralizar la lógica de negocio relacionada con la administración
 * de usuarios dentro del sistema de consumo energético (Administradores, Dueños de hogares y Miembros).
        * </p>
        * Maneja el registro seguro con cifrado de contraseñas (BCrypt), validaciones de unicidad de correos,
 * asociación de miembros a hogares específicos y control de accesos basados en roles mediante Spring Security.
        *
        * @author SQA Team
        * @version 1.0
        */

@Service
@AllArgsConstructor
public class UsuarioServiceimpl  implements UsuarioService {

    private final UsuarioCreateMapper usuarioCreateMapper;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HogarRepository  hogarRepository;


    /*
     * Valida que el correo electrónico cumpla con los formatos requeridos y no se encuentre
     * previamente registrado en la base de datos para evitar duplicidades.
     *
     * @param email Correo electrónico a verificar.
     * @throws ValidationException Si el email es nulo, está vacío (tras aplicar trim) o ya existe.
     */
    private void validarEmailUnico(String email) {
        // Validación de espacios vacíos mediante trim() para evitar cadenas con espacios en blanco inválidos
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("El email es obligatorio y no puede estar vacío");
        }
        if (usuarioRepository.findByEmail(email) != null) {
            throw new ValidationException("El email ya se encuentra registrado");
        }
    }

    /**
     * Registra un nuevo usuario con rol de Administrador en el sistema, aplicando encriptación
     * a su contraseña mediante BCrypt.
     *
     * @param dtOadmin Objeto DTO con los datos de acceso del administrador.
     * @return El DTO recibido tras completar el registro exitoso.
     */
    @Override
    public DTOadmin crearAdmin(DTOadmin dtOadmin){
        validarEmailUnico(dtOadmin.getEmail());
        // Cifrado robusto de la contraseña del administrador utilizando BCrypt con salt automático
        String encryptedPassword = bCryptPasswordEncoder.encode(dtOadmin.getContraseña());
        Usuarios usuarios = new Usuarios();
        usuarios.setEmail(dtOadmin.getEmail());
        usuarios.setPassword(encryptedPassword);
        usuarios.setRole(Role.ADMIN);
        usuarioRepository.save(usuarios);
        return dtOadmin;
    }

    /**
     * Registra un nuevo usuario principal (Dueño de hogar), validando su correo, encriptando su contraseña
     * y asociándolo directamente al hogar especificado por su ID.
     *
     * @param dtOusuarios Datos del usuario a registrar.
     * @param id_hogar    Identificador único del hogar al que pertenecerá el usuario.
     * @return El DTO del usuario creado.
     */

    @Override
public DTOusuarios crearUsuarios(DTOusuarios dtOusuarios, Long id_hogar){
        validarEmailUnico(dtOusuarios.email());

        Hogar hogar = hogarRepository.findById(id_hogar)
                .orElseThrow(() -> new ValidationException("El hogar no existe"));

// Encriptación de la credencial de acceso para el usuario principal o dueño del hogar
        String encryptedPassword = bCryptPasswordEncoder.encode(dtOusuarios.contraseña());

    Usuarios usuarios =  usuarioCreateMapper.toEntity(dtOusuarios);
    usuarios.setPassword(encryptedPassword);
    usuarios.setHogar(hogar);
    usuarioRepository.save(usuarios);
  return dtOusuarios;
}

    /**
     * Registra un miembro adicional dentro de un hogar existente en el sistema.
     *
     * @param dtoMiembro Objeto con la información del miembro del hogar.
     * @return El DTO del miembro registrado.
     */
    @Override
    public DTOMiembro crearMiembrosHogar(DTOMiembro dtoMiembro) {

        Hogar hogar = hogarRepository.findById(dtoMiembro.getHogar_id())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));


        Usuarios miembro = usuarioCreateMapper.toEntity(dtoMiembro);
        miembro.setHogar(hogar);
        usuarioRepository.save(miembro);
        return dtoMiembro;
    }

    /**
     * Actualiza la información personal básica (nombres, apellidos y teléfono) del usuario
     * que se encuentra actualmente autenticado en la sesión.
     *
     * @param dtoUpdateUsuario Objeto con los nuevos datos personales.
     */
    @Override
    public void updateUsuarios(DTOUpdateUsuario dtoUpdateUsuario) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios auth = (Usuarios) authentication.getPrincipal();


            Usuarios usuario = usuarioRepository.findById(auth.getIdUsuario())
                    .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

            usuario.setNombres(dtoUpdateUsuario.getNombres());
            usuario.setApellidos(dtoUpdateUsuario.getApellidos());
            usuario.setTelefono(dtoUpdateUsuario.getTelefono());

            usuarioRepository.save(usuario);


    }
    /**
     * Obtiene y retorna los datos detallados del perfil del usuario autenticado actualmente en sesión.
     *
     * @return Un DTO con la información de nombres, apellidos, correo y teléfono del usuario.
     */

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
    /**
     * Permite exclusivamente al usuario con rol DUEÑO configurar y asignar el correo electrónico definitivo
     * que utilizará un miembro de su hogar para iniciar sesión en la aplicación móvil.
     *
     * @param dtoEmailMiebro Nuevo correo a asignar al miembro.
     * @param id             Identificador único del miembro a actualizar.
     */

@Override
public void SetearEmailMiebro(DTOEmailMiebro dtoEmailMiebro,Long id){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuarios Auth = (Usuarios) authentication.getPrincipal();

    if(Auth.getRole()!= Role.DUEÑO){
        throw new ValidationException("Solo lo dueños de los hogares pueden hacer esta accion");
    }

    validarEmailUnico(dtoEmailMiebro.email());


    Usuarios  miembro = usuarioRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Miembro no encontrado."));



    Hogar hogarDelDueno = Auth.getHogar();
    if (hogarDelDueno == null) {
        throw new ValidationException("El dueño no tiene un hogar registrado.");
    }
    if (!miembro.getHogar().getIdHogar().equals(hogarDelDueno.getIdHogar())) {
        throw new ValidationException("El miembro no pertenece al mismo hogar del dueño.");
    }


    miembro.setEmail(dtoEmailMiebro.email());
    miembro.setAutorizado(true);
    usuarioRepository.save(miembro);

}

    /**
     * Actualiza la contraseña de acceso del usuario autenticado actual, aplicando cifrado BCrypt.
     *
     * @param dtoUpdatePassword Objeto que contiene la nueva contraseña.
     */
public void  actualizarPasword(DTOUpdatePassword dtoUpdatePassword){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuarios Auth = (Usuarios) authentication.getPrincipal();

    String encryptedPassword = bCryptPasswordEncoder.encode(dtoUpdatePassword.getPassword());

        Usuarios usuario = usuarioRepository.findById(Auth.getIdUsuario())
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

        usuario.setPassword(encryptedPassword);
        usuarioRepository.save(usuario);
    }
    /**
     * Comprueba que el correo electrónico del miembro cumpla con las validaciones de unicidad
     * y exista en el sistema.
     *
     * @param dtoEmailMiebro Correo del miembro a comprobar.
     */
    @Override
    public void ComprobarEmailMiebro(DTOEmailMiebro dtoEmailMiebro) {

        validarEmailUnico(dtoEmailMiebro.email());

        if (usuarioRepository.findByEmail(dtoEmailMiebro.email()) == null) {
            throw new ValidationException("Email no existe ");

        }


    }

    /**
     * Establece y encripta la contraseña inicial para un miembro del hogar utilizando su correo electrónico.
     *
     * @param dto   Objeto que contiene la contraseña a configurar.
     * @param email Correo electrónico asociado al miembro.
     */
    @Override
    @Transactional
    public void setearPasswordMiembro(DTOPasswordMiebro dto, String email) {
        Usuarios miembro = usuarioRepository.findByEmail(email);
        if(miembro == null){
            throw new ValidationException("El email no está asociado a ningún miembro");
        }

        String encryptedPassword = bCryptPasswordEncoder.encode(dto.password());
        miembro.setPassword(encryptedPassword);

        usuarioRepository.save(miembro);
    }
    }


