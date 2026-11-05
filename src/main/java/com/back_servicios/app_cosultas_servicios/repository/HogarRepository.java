package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HogarRepository extends JpaRepository<Hogar, Long> {

    /**
     * Busca y retorna el hogar asociado a un usuario a través del ID de su cuenta.
     * Permite identificar rápidamente a qué vivienda pertenece el usuario autenticado en la sesión.
     */
    Optional<Hogar> findByUsuarios_IdUsuario(Long idUsuario);

}
