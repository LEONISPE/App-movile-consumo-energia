package com.back_servicios.app_cosultas_servicios.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

public class SpringDocConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("APP Movile")
                        .description("API Rest de la aplicación consumo de servicios")
                        .contact(new Contact()
                                .name("Equipo worker")
                                .email("worker@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://app.back/api/licencia")));
    }

    @Bean
    public String message()
    {
        System.out.println("Bearer activo");
        return "Bearer activo";
    }

}
