package com.back_servicios.app_cosultas_servicios.domain.entity;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Categoria;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Persona implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    private int edad;

    private String email;
    private String telefono;
    private String password;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private Boolean autorizado = false;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "hogar_id", nullable = false)
    private Hogar hogar;

    @Enumerated(EnumType.STRING)
    private Role role = Role.MIEMBRO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_MIEMBRO"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
