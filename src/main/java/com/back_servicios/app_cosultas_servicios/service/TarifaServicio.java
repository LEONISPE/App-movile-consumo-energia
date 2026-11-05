package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.request.DTOprecioTarifa;
import com.back_servicios.app_cosultas_servicios.domain.entity.Servicios;
import com.back_servicios.app_cosultas_servicios.domain.entity.Tarifa_Servicio;
import com.back_servicios.app_cosultas_servicios.domain.entity.Usuarios;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.PrecioTarifaRepository;
import com.back_servicios.app_cosultas_servicios.repository.ServiciosRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * <h1>Servicio de Gestión de Tarifas</h1>
 * <p>
 * Clase encargada de administrar los precios, rangos de vigencia temporal y asignación de tarifas
 * asociadas a cada servicio público (agua, luz, gas) dentro del sistema.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */



@Service
@AllArgsConstructor
public class TarifaServicio {

    private PrecioTarifaRepository precioTarifaRepository;
    private ServiciosRepository serviciosRepository;

    /**
     * Crea y registra una nueva tarifa de precio para un servicio público específico,
     * validando previamente la existencia del servicio en la base de datos.
     *
     * @param dtoprecioTarifa Objeto DTO que contiene el precio por unidad, fechas de vigencia y el ID del servicio.
     * @return El DTO de la tarifa creada.
     * @throws ValidationException Si el servicio asociado no se encuentra registrado.
     */
    public DTOprecioTarifa crearTarifa(DTOprecioTarifa dtoprecioTarifa) {
        Servicios servicio = serviciosRepository.findById(dtoprecioTarifa.getServicio_id())
                .orElseThrow(() -> new ValidationException("Servicio  no encontrado"));


        Tarifa_Servicio nuevaTarifa = new Tarifa_Servicio();
        nuevaTarifa.setPreciounidad(dtoprecioTarifa.getPreciounidad());
        nuevaTarifa.setFechaInicio(dtoprecioTarifa.getFechaInicio());
        nuevaTarifa.setFechaFin(dtoprecioTarifa.getFechaFin());
        nuevaTarifa.setServicios(servicio);


        precioTarifaRepository.save(nuevaTarifa);

        return dtoprecioTarifa;
    }

    /**
     * Modifica y actualiza los valores de precio y periodos de vigencia de una tarifa de servicio existente.
     *
     * @param idTarifa        Identificador único de la tarifa que se desea modificar.
     * @param dtoprecioTarifa Objeto DTO con los nuevos datos actualizados (precio, fechas y servicio).
     * @return El DTO con la información modificada.
     * @throws ValidationException Si el servicio o la tarifa especificada no existen en el sistema.
     */

    public DTOprecioTarifa modificarPrecioTarifaAgua(Long idTarifa, DTOprecioTarifa dtoprecioTarifa) {

        Servicios servicios = serviciosRepository.findById(dtoprecioTarifa.getServicio_id())
                .orElseThrow(() -> new ValidationException("Servicio no encontrado"));

        Tarifa_Servicio tarifaServicio = precioTarifaRepository.findById(idTarifa)
                .orElseThrow(() -> new ValidationException("Tarifa no encontrada"));

        tarifaServicio.setPreciounidad(dtoprecioTarifa.getPreciounidad());
        tarifaServicio.setFechaInicio(dtoprecioTarifa.getFechaInicio());
        tarifaServicio.setFechaFin(dtoprecioTarifa.getFechaFin());
        tarifaServicio.setServicios(servicios);

        precioTarifaRepository.save(tarifaServicio);

        return dtoprecioTarifa;
    }

}