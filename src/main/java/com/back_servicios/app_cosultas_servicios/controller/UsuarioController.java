package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.*;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOusuariosResponse;
import com.back_servicios.app_cosultas_servicios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*", allowCredentials = "true")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UsuarioController {

    private final UsuarioService usuarioService;



    @Operation(
            summary = "Registrar admin",
            description = "Permite registrar un nuevo usuario administrador  con rol por defecto de admin."
    )
    @PostMapping("/registrar/admin")
    public ResponseEntity<DTOadmin> registrarAdmin(@RequestBody @Valid DTOadmin dto) {
        DTOadmin dtOadmin = usuarioService.crearAdmin(dto);
        return ResponseEntity.ok(dtOadmin);
    }

    @Operation(
            summary = "Registrar usuario",
            description = "Permite registrar un nuevo usuario con rol por defecto de dueño."
    )
    @PostMapping("/registrar/usuario/{id_hogar}")
    public ResponseEntity<DTOusuarios> crearusuario(@RequestBody @Valid DTOusuarios dtOusuarios,@PathVariable Long id_hogar) {
       DTOusuarios dtOusuarios1 = usuarioService.crearUsuarios(dtOusuarios, id_hogar);
         return ResponseEntity.ok(dtOusuarios1);

    }
    @Operation(
            summary = "Registrar Miembro",
            description = "Permite registrar un miembro a un hohar."
    )
    @PostMapping("/registrar/miembro")
    public ResponseEntity<DTOMiembro> crearMiembro(@RequestBody @Valid DTOMiembro dtoMiembro) {
        DTOMiembro dtoMiembro1 = usuarioService.crearMiembrosHogar(dtoMiembro);
        return ResponseEntity.ok(dtoMiembro1);
    }



        @Operation(
            summary = "actualizar usuario",
            description = "Permite actulizar un usuario existente."
    )
    @PutMapping("/update/usuario")
    public ResponseEntity<String> actualizarusuario(@RequestBody DTOUpdateUsuario dtoUpdateUsuario){
        usuarioService.updateUsuarios(dtoUpdateUsuario);
        System.out.println("Actualizando usuario: " + dtoUpdateUsuario.getNombres());
        return ResponseEntity.ok("Usuario actualizado correctamente");

    }

    @Operation(
            summary = "obtener datos del usuario",
            description = "metodo get para obtener datos del usuario"
    )
    @GetMapping("/usuarios")
    public ResponseEntity<DTOusuariosResponse> obtenerusuario() {
            DTOusuariosResponse dtOusuariosResponse = usuarioService.getUsuarios();
            return ResponseEntity.ok(dtOusuariosResponse);

    }

    @Operation(
            summary = "Setear email de miembro",
            description = "Permite Setarle un email a el miembro."
    )
    @PutMapping("/setear/email/{id}")
    public ResponseEntity<DTOEmailMiebro> SetearEmailMiembro( @RequestBody DTOEmailMiebro dtoEmailMiebro,@PathVariable Long id){
        usuarioService.SetearEmailMiebro(dtoEmailMiebro,id);
        return ResponseEntity.ok(dtoEmailMiebro);

    }

    @Operation(
            summary = "Comprobar email de miembro",
            description = "Permite Comprobar el  email a el miembro."
    )
    @PostMapping("/comprobar/email/miembros")
    public ResponseEntity<DTOEmailMiebro> ComprobarEmailMiembro(@RequestBody DTOEmailMiebro dtoEmailMiebro){
        usuarioService.ComprobarEmailMiebro(dtoEmailMiebro);
        return ResponseEntity.ok(dtoEmailMiebro);

    }
    @Operation(
            summary = "Setear password de miembro",
            description = "Permite Setarle  el  Password a el miembro."
    )
    @PostMapping("/miembro/set-password")
    public ResponseEntity<String> actualizarPasswordMiembro(
            @RequestParam String email,
            @RequestBody DTOPasswordMiebro dto) {

        usuarioService.setearPasswordMiembro(dto, email);
        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }


    @Operation(
            summary = "actualizar contraseña del usuario o miembro",
            description = "Permite actualizar contraseña ."
    )
    @PutMapping("/update/password")
    public ResponseEntity<DTOUpdatePassword> ActualizarPassword(@RequestBody DTOUpdatePassword dtoUpdatePassword) {
        usuarioService.actualizarPasword(dtoUpdatePassword);
        return ResponseEntity.ok(dtoUpdatePassword);

    }

    }
