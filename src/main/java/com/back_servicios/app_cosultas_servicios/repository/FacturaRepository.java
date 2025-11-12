package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Factura;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {



    @Query("SELECT AVG(f.consumoTotal) " +
            "FROM Factura f " +
            "WHERE f.hogar.idHogar = :hogarId " +
            "AND f.servicios.servicios = :servicioEnum")
    BigDecimal calcularPromedioConsumo(@Param("hogarId") Long hogarId,
                                       @Param("servicioEnum") ServiciosEnum servicioEnum);




    Optional<Factura> findTopByHogar_IdHogarAndServicios_ServiciosOrderByFechaPeriodoFinDesc(Long hogarId, ServiciosEnum servicioEnum);

    @Query("""
       SELECT f
       FROM Factura f
       JOIN f.hogar h
       JOIN h.usuarios u
       WHERE u.idUsuario = :usuarioId
       AND f.servicios.servicios = :servicio
       """)
    List<Factura> findByUsuarioAndServicio(
            @Param("usuarioId") Long usuarioId,
            @Param("servicio") ServiciosEnum servicio
    );

}
