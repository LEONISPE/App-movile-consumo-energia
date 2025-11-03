package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOfactura;
import com.back_servicios.app_cosultas_servicios.domain.entity.Factura;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;

import java.util.List;

public interface FacturaService {
    void crearFactura(DTOfactura dtOfactura);
    List<Factura> obtenerFacturasPorUsuarioYServicio(Long usuarioId, ServiciosEnum servicio);
}
