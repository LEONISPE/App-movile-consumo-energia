package com.back_servicios.app_cosultas_servicios.controller;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOusuarios;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOusuariosResponse;
import com.back_servicios.app_cosultas_servicios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @Operation(
            summary = "Registrar usuario",
            description = "Permite registrar un nuevo usuario con rol por defecto de dueño."
    )
    @PostMapping("/registrar/usuario")
    public ResponseEntity<String> crearusuario(@RequestBody DTOusuarios dtOusuarios){
         usuarioService.crearUsuarios(dtOusuarios);
         return ResponseEntity.ok("Usuario creado correctamente");

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
