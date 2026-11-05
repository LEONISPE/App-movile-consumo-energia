package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOmiebrosHogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.HogarCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos.IntegrantesFamilia;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * <h1>Implementación del servicio de gestión de Hogares</h1>
 * <p>
 * Clase de servicio central para la administración de hogares, cálculo de factores demográficos
 * de los integrantes (adultos, jóvenes, niños) y consulta de miembros asociados al grupo familiar
 * del usuario autenticado.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */

@Service
@AllArgsConstructor
public class HogarServiceimpl implements HogarService {

    private final HogarCreateMapper hogarCreateMapper;
    private final HogarRepository hogarRepository;
    private final UsuarioRepository usuarioRepository;


    /**
     * Calcula y establece los datos demográficos iniciales del hogar (número de integrantes originales
     * y la suma total de factores ponderados según la categoría de cada integrante).
     *
     * @param hogar Instancia del hogar a procesar y actualizar.
     */
    private void calcularDatosInicialesHogar(Hogar hogar) {
        List<Usuarios> personas = usuarioRepository.findByHogarIdHogar(hogar.getIdHogar());
        int totalIntegrantes = personas.size();
        hogar.setNumeroIntegrantesOriginal(totalIntegrantes);

        // 3️⃣ Calculamos la suma de factores según las categorías
        // Suma iterativa de los factores ponderados según la categoría familiar de cada integrante
        double sumaFactores = 0.0;

        for (Usuarios p : personas) {
            sumaFactores += IntegrantesFamilia.getFactor(p.getCategoria());
        }

        hogar.setSumaFactoresOriginales(sumaFactores);
    }


    /**
     * Obtiene una lista con la información resumida de todos los miembros que pertenecen al hogar
     * del usuario autenticado actualmente en la sesión.
     *
     * @return Lista de DTOs con los datos de los miembros (ID, nombres, email y rol).
     * @throws ValidationException Si el usuario en sesión no tiene un hogar asociado.
     */
    @Override
    public List<DTOmiebrosHogar> obtenerMiembrosDelHogar() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios usuarioAutenticado = (Usuarios) authentication.getPrincipal();

        Hogar hogar = usuarioAutenticado.getHogar();
        if (hogar == null) {
            throw new ValidationException("El usuario no pertenece a ningún hogar");
        }

        Long idHogar = hogar.getIdHogar();

        List<Usuarios> usuarios = usuarioRepository.findByHogarIdHogar(idHogar);


// Flujo Stream de Java para transformar la lista de entidades Usuarios en una lista de DTOmiebrosHogar
        return usuarios.stream()
                .map(u -> new DTOmiebrosHogar(
                        u.getIdUsuario(),
                        u.getNombres(),
                        u.getEmail(),
                        u.getRole()
                ))
                .toList();
    }


    /**
     * Crea un nuevo hogar en el sistema, ejecuta el cálculo de sus datos demográficos iniciales
     * basados en sus integrantes y actualiza el registro guardado.
     *
     * @param dtohogar Objeto DTO con los datos del hogar a registrar.
     * @return El DTO del hogar creado.
     */
    @Override
public DTOhogar createHogar(DTOhogar dtohogar) {

    Hogar hogar = hogarCreateMapper.toEntity(dtohogar);
        hogarRepository.save(hogar);
        calcularDatosInicialesHogar(hogar);
        hogarRepository.save(hogar);

        return dtohogar;
    }

}
