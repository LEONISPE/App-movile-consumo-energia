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

    /**
     * Calcula el promedio total de consumo de las facturas históricas registradas para un hogar y servicio determinados.
     * Sirve como base matemática para proyectar el consumo y costo estimado del ciclo actual.
     */
    @Query("SELECT AVG(f.consumoTotal) " +
            "FROM Factura f " +
            "WHERE f.hogar.idHogar = :hogarId " +
            "AND f.servicios.servicios = :servicioEnum")
    BigDecimal calcularPromedioConsumo(@Param("hogarId") Long hogarId,
                                       @Param("servicioEnum") ServiciosEnum servicioEnum);


    /**
     * Busca y retorna la factura más reciente de un hogar y servicio ordenadas de forma descendente
     * según la fecha de finalización del periodo facturado.
     */

    Optional<Factura> findTopByHogar_IdHogarAndServicios_ServiciosOrderByFechaPeriodoFinDesc(Long hogarId, ServiciosEnum servicioEnum);

    /**
     * Obtiene el listado completo de facturas asociadas a un usuario específico filtradas por un servicio público determinado.
     */
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
