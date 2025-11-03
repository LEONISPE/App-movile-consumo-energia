package com.back_servicios.app_cosultas_servicios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AppCosultasServiciosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppCosultasServiciosApplication.class, args);
	}

}
