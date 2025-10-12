package com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos;

import java.time.Month;
import java.util.Map;

public class Estacionalidad {

    private static final Map<Month, Double> FACTORES = Map.of(
            Month.JANUARY, 1.10,   // +10% vacaciones
            Month.FEBRUARY, 0.95,  // -5% más tranquilo
            Month.MARCH, 1.08,     // +8% Semana Santa
            Month.APRIL, 1.05,
            Month.JUNE, 1.20,      // +20% vacaciones escolares
            Month.JULY, 1.10,
            Month.DECEMBER, 1.15   // +15% festividades
    );

    public static double getFactor(Month mes) {
        return FACTORES.getOrDefault(mes, 1.0);
    }
}

