package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOadmin;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOusuarios;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOusuariosResponse;
import com.back_servicios.app_cosultas_servicios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/registrar/usuario")
    public ResponseEntity<DTOusuarios> crearusuario(@RequestBody @Valid DTOusuarios dtOusuarios){
       DTOusuarios dtOusuarios1 = usuarioService.crearUsuarios(dtOusuarios);
         return ResponseEntity.ok(dtOusuarios1);

    }

    @Operation(
            summary = "actualizar usuario",
            description = "Permite actulizar un usuario existente."
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarusuario(@PathVariable Long id,@RequestBody DTOusuarios dtOusuarios){
        usuarioService.updateUsuarios(id,dtOusuarios);
        return ResponseEntity.ok("Usuario actualizado correctamente");

    }

    @GetMapping("/{id}")
    public ResponseEntity<DTOusuariosResponse> obtenerusuario(@PathVariable Long id) {
        DTOusuariosResponse dtOusuariosResponse1 = usuarioService.getUsuarios(id);
        return ResponseEntity.ok(dtOusuariosResponse1);


    }

    }
