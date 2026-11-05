package com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos;

import java.time.Month;
import java.util.Map;

/**
 * Clase utilitaria que gestiona los factores de estacionalidad por mes.
 * Modela la variación estimada en el consumo de servicios públicos (agua, energía, gas)
 * considerando costumbres estacionales, épocas de vacaciones, festividades o cambios climáticos
 * que influyen en el incremento o disminución del promedio de gasto en los hogares.
 */
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

    /**
     * Retorna el factor multiplicador de consumo correspondiente a un mes específico.
     * Si el mes no está mapeado explícitamente, retorna un factor neutral de 1.0.
     */
    public static double getFactor(Month mes) {
        return FACTORES.getOrDefault(mes, 1.0);
    }
}

