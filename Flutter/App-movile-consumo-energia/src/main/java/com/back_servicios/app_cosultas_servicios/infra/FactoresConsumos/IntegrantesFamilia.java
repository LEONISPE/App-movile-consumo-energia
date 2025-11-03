package com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos;

import com.back_servicios.app_cosultas_servicios.domain.entity.Hogar;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Categoria;

import java.util.Map;

public class IntegrantesFamilia {

    private static final Map<Categoria, Double> FACTORES = Map.of(
            Categoria.NINO, 0.6,   // cada niño aporta 60% del consumo base
            Categoria.JOVEN, 1.0,  // cada joven = 100%
            Categoria.ADULTO, 1.3  // cada adulto consume un 30% más
    );

    public static double getFactor(Categoria categoria) {
        return FACTORES.getOrDefault(categoria, 1.0);
    }

    // 👇 método para calcular el factor total según los miembros actuales
    public static double calcularFactorHogar(Hogar hogar) {
        if (hogar.getPersonas() == null || hogar.getPersonas().isEmpty()) {
            return 1.0; // sin datos de personas, se mantiene el consumo base
        }

        // 1️⃣ calcular la suma de los factores de todos los miembros actuales
        double sumaFactoresActuales = hogar.getPersonas().stream()
                .mapToDouble(p -> getFactor(p.getCategoria()))
                .sum();

        // 2️⃣ obtener cuántas personas había originalmente
        int integrantesOriginales = hogar.getNumeroIntegrantesOriginal();
        if (integrantesOriginales == 0) return 1.0;

        // 3️⃣ normalizar el factor actual frente al original
        // ejemplo: antes 4 personas (total factor = 4.6), ahora 5 (factor = 5.9)
        // => 5.9 / 4.6 = 1.28  → 28% más de consumo
        double sumaFactoresOriginales = integrantesOriginales; // asumimos promedio 1.0 por persona original

        return sumaFactoresActuales / sumaFactoresOriginales;
    }
}
