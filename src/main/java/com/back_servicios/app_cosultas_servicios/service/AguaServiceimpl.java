package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioAgua;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOconsumoAcomulado;
import com.back_servicios.app_cosultas_servicios.domain.entity.*;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.repository.ConsumoServicioRepository;
import com.back_servicios.app_cosultas_servicios.repository.FacturaRepository;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.ServiciosRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class AguaServiceimpl implements AguaService {
    private final ConsumoServicioRepository consumoServicioRepository;
    private final HogarRepository hogarRepository;
    private final FacturaRepository facturaRepository;
    private final ServiciosRepository serviciosRepository;

    private static final BigDecimal TARIFA_AGUA = BigDecimal.valueOf(1400);

    /**
     * Calcular consumo esperado en tiempo real para el hogar autenticado
     */
    @Override
    public ConsumoDTODiarioAgua calcularConsumoActual() {
        // 1. Obtener usuario autenticado -> su hogar
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuarios principal = (Usuarios) auth.getPrincipal();
        Hogar hogar = hogarRepository.findByUsuarioIdUsuario(principal.getIdUsuario())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));

        // 2. Calcular promedio histórico (ejemplo: últimas 2 facturas)
        BigDecimal promedio = facturaRepository.calcularPromedioConsumo(hogar.getIdHogar());
        if (promedio == null) promedio = BigDecimal.ZERO;

        // 3. Derivar consumo diario y por hora
        int diasMes = LocalDate.now().lengthOfMonth();
        BigDecimal consumoDiario = promedio.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 4, RoundingMode.HALF_UP);

        // 4. Calcular consumo acumulado del día (según la hora actual)
        int horasTranscurridas = LocalDateTime.now().getHour(); // 0-23
        BigDecimal consumoActualHoy = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas));

        // 5. Calcular costo
        BigDecimal costoActualHoy = consumoActualHoy.multiply(TARIFA_AGUA);

        // 6. Retornar DTO con info para mostrar al usuario
        return new ConsumoDTODiarioAgua(
                consumoActualHoy,
                costoActualHoy,
                consumoHora

        );

    }

    @Override
    public DTOconsumoAcomulado obtenerConsumoAcomulado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuarios principal = (Usuarios) auth.getPrincipal();
        Hogar hogar = hogarRepository.findByUsuarioIdUsuario(principal.getIdUsuario())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));
// 1. Calcular promedio histórico de facturas
        BigDecimal promedio = facturaRepository.calcularPromedioConsumo(hogar.getIdHogar());
        if (promedio == null) promedio = BigDecimal.ZERO;

        // 2. Derivar consumo por hora
        int diasMes = LocalDate.now().lengthOfMonth();
        BigDecimal consumoDiario = promedio.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 4, RoundingMode.HALF_UP);

        // 3. Determinar fecha de inicio del recibo
        LocalDateTime ahora = LocalDateTime.now();
        LocalDate inicioRecibo = LocalDate.of(ahora.getYear(), ahora.getMonth(), 25);
        LocalDateTime inicioReciboMesActual = inicioRecibo.atStartOfDay();
        if (ahora.isBefore(inicioReciboMesActual)) {
            inicioReciboMesActual = inicioRecibo.minusMonths(1).atStartOfDay();
        }

        // 4. Calcular horas transcurridas desde inicio del recibo
        long horasTranscurridas = ChronoUnit.HOURS.between(inicioReciboMesActual, ahora);

        // 5. Calcular acumulado
        BigDecimal consumoAcumulado = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas));
        BigDecimal costoAcumulado = consumoAcumulado.multiply(TARIFA_AGUA);

        // 6. Retornar DTO
        DTOconsumoAcomulado dto = new DTOconsumoAcomulado();
        dto.setConsumoAcomulado(consumoAcumulado);
        dto.setCostoAcomulado(costoAcumulado);

        return dto;
    }



    /**
     * Guardar consumo diario (ejecutado cada medianoche con @Scheduled)
     */
    @Scheduled(cron = "0 0 0 * * *") // todos los días a medianoche
    public void guardarConsumoDiario() {
        LocalDate hoy = LocalDate.now().minusDays(1); // día que terminó
        List<Hogar> hogares = hogarRepository.findAll();

        for (Hogar hogar : hogares) {
            BigDecimal promedio = facturaRepository.calcularPromedioConsumo(hogar.getIdHogar());
            if (promedio == null) promedio = BigDecimal.ZERO;

            int diasMes = hoy.lengthOfMonth();
            BigDecimal consumoDiario = promedio.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
            BigDecimal costoDiario = consumoDiario.multiply(TARIFA_AGUA);

            Servicios servicioAgua = serviciosRepository.findByServicios(ServiciosEnum.AGUA)
                    .orElseThrow(() -> new ValidationException("Servicio Agua no encontrado"));

            Consumo_Servicio consumo = new Consumo_Servicio();
            consumo.setHogar(hogar);
            consumo.setServicios(servicioAgua);
            consumo.setFecha(hoy);
            consumo.setConsumo(consumoDiario);
            consumo.setCosto(costoDiario);

            consumoServicioRepository.save(consumo);
        }
    }

    // Se ejecuta cada día a la medianoche
    @Scheduled(cron = "0 0 0 * * *")
    public void cerrarFacturasMensuales() {
        LocalDate hoy = LocalDate.now();

        // Ejemplo: verificamos si hoy es día 25
        if (hoy.getDayOfMonth() == 25) {
            // Aquí recorres los hogares y servicios
            for (Hogar hogar : hogarRepository.findAll()) {
                for (Servicios servicio : serviciosRepository.findAll()) {

                    // Definir periodo de la factura que acaba hoy
                    LocalDate fechaInicio = hoy.minusMonths(1); // arranca el 25 del mes pasado
                    LocalDate fechaFin = hoy;                   // termina hoy

                    // Calcular consumo total (ejemplo: promedio * 30 días)
                    BigDecimal consumoPromedio = facturaRepository.calcularPromedioConsumo(hogar.getIdHogar());
                    if (consumoPromedio == null) consumoPromedio = BigDecimal.ZERO;// aquí usas tu lógica real
                    BigDecimal consumoTotal = consumoPromedio.multiply(BigDecimal.valueOf(30));

                    // Calcular costo total
                    BigDecimal costoTotal = consumoTotal.multiply(TARIFA_AGUA);

                    // Crear y guardar factura
                    Factura factura = new Factura();
                    factura.setFecha_periodoInicio(fechaInicio);
                    factura.setFecha_periodoFin(fechaFin);
                    factura.setConsumoTotal(consumoTotal);
                    factura.setCosto_total(costoTotal);
                    factura.setHogar(hogar);
                    factura.setServicios(servicio);

                    facturaRepository.save(factura);
                }
            }
        }
    }
}