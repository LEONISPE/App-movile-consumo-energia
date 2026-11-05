package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Tarifa_Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PrecioTarifaRepository extends JpaRepository<Tarifa_Servicio,Long> {

    /** Busca la tarifa asociada directamente al ID de un servicio específico. */
    Optional<Tarifa_Servicio> findByServiciosId(Long servicioId);

    /**
     * Consulta inteligente para encontrar la **tarifa vigente** de un servicio en la fecha actual.
     * Valida que la fecha actual se encuentre dentro del rango (fecha de inicio menor o igual a hoy,
     * y fecha de fin nula o mayor/igual a hoy), ordenándolas de manera descendente para priorizar la más reciente.
     */
    @Query("SELECT t FROM Tarifa_Servicio t " +
            "WHERE t.servicios = :servicio " +
            "AND (t.fechaFin IS NULL OR t.fechaFin >= CURRENT_DATE) " +
            "AND t.fechaInicio <= CURRENT_DATE " +
            "ORDER BY t.fechaInicio DESC")
    Optional<Tarifa_Servicio> findTarifaVigente(@Param("servicio") Servicios servicio);
}
