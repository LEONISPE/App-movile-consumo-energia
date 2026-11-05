package com.back_servicios.app_cosultas_servicios.domain.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    /**
     * Administrador del sistema.
     * Encargado de la gestión global, creación de hogares, registro de usuarios,
     * carga de tarifas y precarga o generación de facturas en la plataforma.
     */
    ADMIN("ROLE_ADMIN"),

    /**
     * Dueño o cabeza de hogar.
     * Rol principal responsable del hogar. Tiene la autoridad exclusiva de autorizar
     * y registrar miembros adicionales de la familia para otorgarles acceso a la aplicación.
     */
    DUEÑO("ROLE_DUEÑO"),

    /**
     * Miembro de la familia.
     * Rol con acceso limitado, ingresa mediante la autorización de un usuario Dueño.
     * Puede consultar los consumos en tiempo real de los servicios públicos, pero no gestionar accesos de terceros.
     */
    MIEMBRO("ROLE_MIEMBRO");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
