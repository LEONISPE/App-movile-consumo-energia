package com.back_servicios.app_cosultas_servicios.repository;

import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuarios,Long> {

    /** Busca y retorna un usuario utilizando su correo electrónico como criterio único de búsqueda. */
    Usuarios findByEmail(String email);

    /** Retorna una lista con todos los usuarios (miembros y dueños) que pertenecen a un hogar específico mediante su ID. */
    List<Usuarios> findByHogarIdHogar(Long id_hogar);
}
