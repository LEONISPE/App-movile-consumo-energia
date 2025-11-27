package com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos;

import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Categoria;

import java.util.List;
import java.util.Map;

public class IntegrantesFamilia {

    private static final Map<Categoria, Double> FACTORES = Map.of(
            Categoria.NINO, 0.6,   // cada niño aporta 60% del consumo base
            Categoria.JOVEN, 1.0,  // cada joven = 100%
            Categoria.ADULTO, 1.3  // cada adulto consume 30% más
    );

    public static double getFactor(Categoria categoria) {
        return FACTORES.getOrDefault(categoria, 1.0);
    }

    // Ahora calcula usando los usuarios del hogar
    public static double calcularFactorHogar(Hogar hogar) {

        List<Usuarios> miembros = hogar.getUsuarios();  // Dueño + miembros

        if (miembros == null || miembros.isEmpty()) {
            return 1.0; // Hogar sin usuarios registrados → consumo base
        }

        // 1️⃣ Sumar los factores de consumo de cada usuario
        double sumaFactoresActuales = miembros.stream()
                .mapToDouble(u -> getFactor(u.getCategoria()))
                .sum();

        // 2️⃣ Leer cuántos integrantes había originalmente registrados
        int integrantesOriginales = hogar.getNumeroIntegrantesOriginal();
        if (integrantesOriginales == 0) return 1.0;

        // 3️⃣ Normalización (consumo actual / consumo inicial esperado)
        double sumaFactoresOriginales = hogar.getSumaFactoresOriginales(); // 1.0 es consumo estándar base

        return sumaFactoresActuales / sumaFactoresOriginales;
    }
}
