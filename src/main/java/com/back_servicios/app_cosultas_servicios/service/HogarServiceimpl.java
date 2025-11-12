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

@Service
@AllArgsConstructor
public class HogarServiceimpl implements HogarService {

    private final HogarCreateMapper hogarCreateMapper;
    private final HogarRepository hogarRepository;
    private final UsuarioRepository usuarioRepository;


    private void calcularDatosInicialesHogar(Hogar hogar) {
        List<Usuarios> personas = usuarioRepository.findByHogarIdHogar(hogar.getIdHogar());
        int totalIntegrantes = personas.size();
        hogar.setNumeroIntegrantesOriginal(totalIntegrantes);

        // 3️⃣ Calculamos la suma de factores según las categorías
        double sumaFactores = 0.0;



        for (Usuarios p : personas) {
            sumaFactores += IntegrantesFamilia.getFactor(p.getCategoria());
        }

        hogar.setSumaFactoresOriginales(sumaFactores);
    }

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


        return usuarios.stream()
                .map(u -> new DTOmiebrosHogar(
                        u.getIdUsuario(),
                        u.getNombres(),
                        u.getEmail(),
                        u.getRole()
                ))
                .toList();
    }


    @Override
public DTOhogar createHogar(DTOhogar dtohogar) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios Auth = (Usuarios) authentication.getPrincipal();

        if(Auth.getRole() != Role.ADMIN){
            throw new ValidationException("El usuario no tiene role administrador");
        }

    Hogar hogar = hogarCreateMapper.toEntity(dtohogar);
        hogarRepository.save(hogar);
        calcularDatosInicialesHogar(hogar);
        hogarRepository.save(hogar);

        return dtohogar;
    }

}
