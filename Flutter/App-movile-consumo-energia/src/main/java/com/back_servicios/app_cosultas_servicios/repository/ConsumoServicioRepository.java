package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Consumo_Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumoServicioRepository extends JpaRepository<Consumo_Servicio,Long> {
}
