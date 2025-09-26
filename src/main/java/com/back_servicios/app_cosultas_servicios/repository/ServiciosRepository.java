package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiciosRepository extends JpaRepository<Servicios,Long> {

    Optional<Servicios> findByServicios(ServiciosEnum servicios);
}
