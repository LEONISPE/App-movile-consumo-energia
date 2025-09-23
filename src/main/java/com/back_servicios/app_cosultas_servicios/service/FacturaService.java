package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOfactura;

public interface FacturaService {
    void crearFactura(DTOfactura dtOfactura);
}
