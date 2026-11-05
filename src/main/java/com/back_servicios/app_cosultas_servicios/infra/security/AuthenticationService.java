package com.back_servicios.app_cosultas_servicios.infra.security;

import com.back_servicios.app_cosultas_servicios.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación que implementa la interfaz UserDetailsService de Spring Security.
 * Se encarga de consultar y recuperar los detalles del usuario en la base de datos
 * utilizando su correo electrónico como identificador único (username).
 */
@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usuarioRepository.findByEmail(username);
    }
}
