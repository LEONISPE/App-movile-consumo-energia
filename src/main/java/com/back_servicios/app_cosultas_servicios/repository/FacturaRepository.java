package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Factura;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface FacturaRepository extends JpaRepository<Factura, Long> {



    @Query("SELECT AVG(f.consumoTotal) " +
            "FROM Factura f " +
            "WHERE f.hogar.idHogar = :hogarId " +
            "AND f.servicios.servicios = :servicioEnum")
    BigDecimal calcularPromedioConsumo(@Param("hogarId") Long hogarId,
                                       @Param("servicioEnum") ServiciosEnum servicioEnum);
}
