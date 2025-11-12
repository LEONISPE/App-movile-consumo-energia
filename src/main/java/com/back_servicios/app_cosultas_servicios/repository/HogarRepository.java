package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HogarRepository extends JpaRepository<Hogar, Long> {
    Optional<Hogar> findByUsuarios_IdUsuario(Long idUsuario);

}
