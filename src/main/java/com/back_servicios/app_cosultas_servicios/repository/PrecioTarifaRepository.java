package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Tarifa_Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrecioTarifaRepository extends JpaRepository<Tarifa_Servicio,Long> {

    Optional<Tarifa_Servicio> findByServiciosId(Long servicioId);
}
