package com.back_servicios.app_cosultas_servicios.domain.enumerated;

public enum Estrato_Gas {
    ESTRATO_1(-0.50), // subsidio 50%
    ESTRATO_2(-0.40), // subsidio 40%
    ESTRATO_3(-0.15), // subsidio 15%
    ESTRATO_4(0.0),   // sin subsidio ni contribución
    ESTRATO_5(0.20),  // contribución 20%
    ESTRATO_6(0.20);  // contribución 20%

    private final double factor;

    Estrato_Gas(double factor) {
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }
}
