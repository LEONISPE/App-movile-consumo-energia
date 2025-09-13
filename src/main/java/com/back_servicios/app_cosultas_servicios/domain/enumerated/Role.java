package com.back_servicios.app_cosultas_servicios.domain.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    ADMIN("ROLE_ADMIN"),
    DUEÑO("ROLE_DUEÑO"),
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
