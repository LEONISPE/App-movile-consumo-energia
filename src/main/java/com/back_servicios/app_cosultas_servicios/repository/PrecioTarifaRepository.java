package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Tarifa_Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PrecioTarifaRepository extends JpaRepository<Tarifa_Servicio,Long> {

    Optional<Tarifa_Servicio> findByServiciosId(Long servicioId);

    @Query("SELECT t FROM Tarifa_Servicio t " +
            "WHERE t.servicios = :servicio " +
            "AND (t.fechaFin IS NULL OR t.fechaFin >= CURRENT_DATE) " +
            "AND t.fechaInicio <= CURRENT_DATE " +
            "ORDER BY t.fechaInicio DESC")
    Optional<Tarifa_Servicio> findTarifaVigente(@Param("servicio") Servicios servicio);
}
