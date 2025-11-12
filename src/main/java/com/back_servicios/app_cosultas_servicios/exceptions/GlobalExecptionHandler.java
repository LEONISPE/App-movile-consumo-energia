package com.back_servicios.app_cosultas_servicios.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Hidden
@RestControllerAdvice
public class GlobalExecptionHandler {

    @Hidden
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacionException(ValidationException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", ex.getMessage());
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(respuesta);
    }
    @Hidden
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcionGeneral(Exception ex) {
        ex.printStackTrace();
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", "Ocurrió un error inesperado. Contacte con soporte.");
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
