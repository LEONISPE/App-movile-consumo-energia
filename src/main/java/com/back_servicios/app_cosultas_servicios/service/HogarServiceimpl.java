package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOhogar;
import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOpersona;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOmiebrosHogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Persona;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.domain.mapper.request.HogarCreateMapper;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos.IntegrantesFamilia;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.PersonaRepository;
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
    private final PersonaRepository personaRepository;

    private void calcularDatosInicialesHogar(Hogar hogar) {
        List<Persona> personas = personaRepository.findByHogarIdHogar(hogar.getIdHogar());

        Usuarios dueño = hogar.getUsuario();

        // Si aún no hay personas registradas (por ejemplo, hogar recién creado)
        if ((personas == null || personas.isEmpty()) && dueño == null) {
            hogar.setNumeroIntegrantesOriginal(0);
            hogar.setSumaFactoresOriginales(0);
            return;
        }


        int totalIntegrantes = personas.size() + (dueño != null ? 1 : 0);
        hogar.setNumeroIntegrantesOriginal(totalIntegrantes);

        // 3️⃣ Calculamos la suma de factores según las categorías
        double sumaFactores = 0.0;


        if (dueño != null && dueño.getCategoria() != null) {
            sumaFactores += IntegrantesFamilia.getFactor(dueño.getCategoria());
        }

        for (Persona p : personas) {
            sumaFactores += IntegrantesFamilia.getFactor(p.getCategoria());
        }

        hogar.setSumaFactoresOriginales(sumaFactores);
    }

    @Override
    public List<DTOmiebrosHogar> obtenerMiembrosDelHogar(Long id_hogar) {


        Optional<Hogar> optionalHogar = hogarRepository.findById(id_hogar);

        if(!optionalHogar.isPresent()){
            throw new ValidationException("El hogar no existe");
        }
        Hogar hogar = optionalHogar.get();

        // Buscar las personas (miembros) asociadas al hogar
        List<Persona> personas = personaRepository.findByHogarIdHogar(id_hogar);
        List<DTOmiebrosHogar> integrantes = new ArrayList<>();





        // Agregar el dueño si existe
        if (hogar.getUsuario() != null) {
            DTOmiebrosHogar dueno = new DTOmiebrosHogar(
                    hogar.getUsuario().getNombres(),
                    hogar.getUsuario().getEmail(),
                    hogar.getUsuario().getRole()
            );
            integrantes.add(dueno);
        }

        // Agregar miembros
        if (personas != null && !personas.isEmpty()) {
            for (Persona persona : personas) {
                integrantes.add(new DTOmiebrosHogar(
                        persona.getNombre(),
                        persona.getEmail(),
                        persona.getRole()
                ));
            }
        }



        return integrantes;
    }


    @Override
public DTOhogar createHogar(DTOhogar dtohogar, Long idUsuario) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios Auth = (Usuarios) authentication.getPrincipal();

        if(Auth.getRole() != Role.ADMIN){
            throw new ValidationException("El usuario no tiene role administrador");
        }

        Usuarios usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado con id: " + idUsuario));


    Hogar hogar = hogarCreateMapper.toEntity(dtohogar);
        System.out.println("Usuario encontrado: " + usuario.getIdUsuario());
    hogar.setUsuario(usuario);

        hogarRepository.save(hogar);


        calcularDatosInicialesHogar(hogar);

        hogarRepository.save(hogar);

        return dtohogar;
    }

}
