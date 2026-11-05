package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.*;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOusuariosResponse;
import com.back_servicios.app_cosultas_servicios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/**
 * <h1>Controlador REST para la Gestión de Usuarios y Accesos</h1>
 * <p>
 * Expone los endpoints de la API REST para el registro de usuarios (Administradores, Dueños de Hogar y Miembros),
 * actualización de información personal, gestión de contraseñas y asignación de credenciales.
 * Integra validaciones de datos con Bean Validation, seguridad por JWT y documentación interactiva Swagger/OpenAPI.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */
@Tag(name = "Gestión de Usuarios", description = "Endpoints para registro, actualización de perfil, asignación de roles y contraseñas")

@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*", allowCredentials = "true")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Permite registrar un nuevo usuario con rol de Administrador.
     *
     * @param dto {@code @RequestBody} recibe la estructura JSON enviada en el cuerpo de la petición HTTP.
     *            {@code @Valid} activa las anotaciones de validación (Bean Validation) declaradas en el DTO
     *            para garantizar que los campos cumplan con el formato correcto antes de entrar a la lógica.
     * @return {@link ResponseEntity} con el DTO del administrador creado y código HTTP 200 OK.
     */
    @Operation(
            summary = "Registrar admin",
            description = "Permite registrar un nuevo usuario administrador  con rol por defecto de admin."
    )
    @PostMapping("/registrar/admin")
    public ResponseEntity<DTOadmin> registrarAdmin(@RequestBody @Valid DTOadmin dto) {
        DTOadmin dtOadmin = usuarioService.crearAdmin(dto);
        return ResponseEntity.ok(dtOadmin);
    }

    /**
     * Registra un usuario principal (Dueño de hogar) vinculado a un ID de hogar específico.
     *
     * @param dtOusuarios Cuerpo JSON validado con la información del nuevo usuario.
     * @param id_hogar    {@code @PathVariable} extrae la variable enviada directamente en la URI de la ruta HTTP (ej. /registrar/usuario/5).
     *                    {@code @PreAuthorize("hasRole('ADMIN')")} evalúa la seguridad con Spring Security, restringiendo
     *                    la ejecución del endpoint exclusivamente a usuarios autenticados con rol ADMIN.
     */
    @Operation(
            summary = "Registrar usuario",
            description = "Permite registrar un nuevo usuario con rol por defecto de dueño."
    )
    @PostMapping("/registrar/usuario/{id_hogar}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DTOusuarios> crearusuario(@RequestBody @Valid DTOusuarios dtOusuarios,@PathVariable Long id_hogar) {
       DTOusuarios dtOusuarios1 = usuarioService.crearUsuarios(dtOusuarios, id_hogar);
         return ResponseEntity.ok(dtOusuarios1);

    }

    /**
     * Permite a un Dueño de hogar registrar un nuevo miembro dentro de su grupo familiar.
     */
    @Operation(
            summary = "Registrar Miembro",
            description = "Permite registrar un miembro a un hohar."
    )
    @PostMapping("/registrar/miembro")
    @PreAuthorize("hasRole('DUEÑO')")
    public ResponseEntity<DTOMiembro> crearMiembro(@RequestBody @Valid DTOMiembro dtoMiembro) {
        DTOMiembro dtoMiembro1 = usuarioService.crearMiembrosHogar(dtoMiembro);
        return ResponseEntity.ok(dtoMiembro1);
    }

    /**
     * Actualiza la información del perfil del usuario autenticado en la sesión actual.
     */
        @Operation(
            summary = "actualizar usuario",
            description = "Permite actulizar un usuario existente."
    )
    @PutMapping("/update/usuario")
    public ResponseEntity<String> actualizarusuario(@RequestBody @Valid DTOUpdateUsuario dtoUpdateUsuario){
        usuarioService.updateUsuarios(dtoUpdateUsuario);
        System.out.println("Actualizando usuario: " + dtoUpdateUsuario.getNombres());
        return ResponseEntity.ok("Usuario actualizado correctamente");

    }

    /**
     * Retorna la información personal del usuario que se encuentra autenticado.
     */
    @Operation(
            summary = "obtener datos del usuario",
            description = "metodo get para obtener datos del usuario"
    )
    @GetMapping("/usuarios")
    public ResponseEntity<DTOusuariosResponse> obtenerusuario() {
            DTOusuariosResponse dtOusuariosResponse = usuarioService.getUsuarios();
            return ResponseEntity.ok(dtOusuariosResponse);

    }

    /**
     * Permite a un usuario con rol DUEÑO asignar y activar el correo electrónico definitivo de un miembro de su hogar.
     *
     * @param dtoEmailMiebro JSON con el email a asignar.
     * @param id             ID del miembro recibido por la ruta (PathVariable).
     */
    @Operation(
            summary = "Setear email de miembro",
            description = "Permite Setarle un email a el miembro."
    )
    @PutMapping("/setear/email/{id}")
    @PreAuthorize("hasRole('DUEÑO')")
    public ResponseEntity<DTOEmailMiebro> SetearEmailMiembro( @RequestBody  @Valid  DTOEmailMiebro dtoEmailMiebro,@PathVariable Long id){
        usuarioService.SetearEmailMiebro(dtoEmailMiebro,id);
        return ResponseEntity.ok(dtoEmailMiebro);

    }

    /**
     * Comprueba la disponibilidad y existencia del correo electrónico asignado a un miembro.
     */
    @Operation(
            summary = "Comprobar email de miembro",
            description = "Permite Comprobar el  email a el miembro."
    )
    @PostMapping("/comprobar/email/miembros")
    public ResponseEntity<DTOEmailMiebro> ComprobarEmailMiembro(@RequestBody  @Valid  DTOEmailMiebro dtoEmailMiebro){
        usuarioService.ComprobarEmailMiebro(dtoEmailMiebro);
        return ResponseEntity.ok(dtoEmailMiebro);

    }

    /**
     * Establece la contraseña de acceso inicial para un miembro usando su dirección de correo electrónico.
     */
    @Operation(
            summary = "Setear password de miembro",
            description = "Permite Setarle  el  Password a el miembro."
    )
    @PostMapping("/miembro/set-password")
    public ResponseEntity<String> actualizarPasswordMiembro(
            @Valid
            @RequestParam String email,
            @RequestBody DTOPasswordMiebro dto) {

        usuarioService.setearPasswordMiembro(dto, email);
        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }

    /**
     * Actualiza la clave privada del usuario o miembro que se encuentra actualmente autenticado.
     */
    @Operation(
            summary = "actualizar contraseña del usuario o miembro",
            description = "Permite actualizar contraseña ."
    )
    @PutMapping("/update/password")
    public ResponseEntity<DTOUpdatePassword> ActualizarPassword(@RequestBody  @Valid DTOUpdatePassword dtoUpdatePassword) {
        usuarioService.actualizarPasword(dtoUpdatePassword);
        return ResponseEntity.ok(dtoUpdatePassword);

    }

    }
