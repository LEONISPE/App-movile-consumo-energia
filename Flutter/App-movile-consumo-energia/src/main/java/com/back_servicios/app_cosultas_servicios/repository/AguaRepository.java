package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AguaRepository  extends JpaRepository<Servicios, Long> {
}
