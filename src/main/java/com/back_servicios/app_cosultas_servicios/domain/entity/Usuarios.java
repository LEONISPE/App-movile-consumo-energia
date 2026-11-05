package com.back_servicios.app_cosultas_servicios.domain.entity;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Categoria;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "usuarios")
public class Usuarios  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;
    private String nombres;
    private int edad;
    private String apellidos;
    private String email;
    private String telefono;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    /**
     * Indica si el usuario se encuentra activo o autorizado dentro del sistema.
     * Permite denegar el acceso a miembros inhabilitados sin necesidad de eliminarlos físicamente de la base de datos.
     */
    private Boolean autorizado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hogar_id")
    @JsonBackReference
    private Hogar hogar;

    /**
     * Asigna los permisos y roles de seguridad basados en Spring Security.
     * Si el usuario no tiene rol asignado, retorna una lista vacía sin privilegios especiales.
     */
   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
       if (role == null) {
           return List.of(); // No tiene rol → no tendrá permisos especiales
       }
       return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
   }


    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retorna el correo electrónico como el identificador único (username) para la autenticación.
     */
    @Override
    public String getUsername() {
       return email;
    }

    /** Indica si la cuenta del usuario no ha expirado (Por defecto true según Spring Security). */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /** Indica si la cuenta del usuario no está bloqueada (Por defecto true según Spring Security). */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /** Indica si las credenciales del usuario no han expirado (Por defecto true según Spring Security). */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /** Indica si el usuario está activo y habilitado en el sistema (Por defecto true según Spring Security). */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
