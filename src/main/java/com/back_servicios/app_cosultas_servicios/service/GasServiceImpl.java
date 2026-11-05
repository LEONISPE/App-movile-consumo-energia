package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioGas;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTOacomuladoGas;

import com.back_servicios.app_cosultas_servicios.domain.entity.*;

import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Gas;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos.Estacionalidad;
import com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos.IntegrantesFamilia;
import com.back_servicios.app_cosultas_servicios.repository.FacturaRepository;
import com.back_servicios.app_cosultas_servicios.repository.HogarRepository;
import com.back_servicios.app_cosultas_servicios.repository.PrecioTarifaRepository;
import com.back_servicios.app_cosultas_servicios.repository.ServiciosRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


/**
 * <h1>Implementación del servicio de cálculo y gestión de Gas</h1>
 * <p>
 * Clase encargada de procesar el consumo actual, acumulado y diario del servicio de gas domiciliario,
 * aplicando factores de estacionalidad mensual, tamaño del hogar y tarifas vigentes bajo la zona horaria de Colombia.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */

@Service
@AllArgsConstructor
public class GasServiceImpl implements GasService {

    private  final ServiciosRepository serviciosRepository;
    private  final HogarRepository hogarRepository;
    private final PrecioTarifaRepository precioTarifaRepository;
    private  final FacturaRepository facturaRepository;

    private static final ZoneId ZONA_COLOMBIA = ZoneId.of("America/Bogota");

    // --- MÉTODOS AUXILIARES PRIVADOS (Principio DRY) ---

    /**
     * Obtiene el usuario autenticado actualmente en la sesión de Spring Security.
     *
     * @return Entidad Usuarios correspondiente al principal autenticado.
     */
    private Usuarios obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuarios) auth.getPrincipal();
    }

    /**
     * Busca y retorna el hogar asociado al usuario especificado por su ID.
     *
     * @param idUsuario Identificador único del usuario.
     * @return Entidad Hogar asociada.
     * @throws ValidationException Si no se encuentra un hogar vinculado al usuario.
     */
    private Hogar obtenerHogarPorUsuario(Long idUsuario) {
        return hogarRepository.findByUsuarios_IdUsuario(idUsuario)
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));
    }

    /**
     * Busca y retorna la entidad de servicio público según el tipo enumerado.
     *
     * @param servicioEnum Tipo de servicio (GAS).
     * @return Entidad Servicios correspondiente.
     * @throws ValidationException Si el servicio no existe en el sistema.
     */
    private Servicios obtenerServicio(ServiciosEnum servicioEnum) {
        return serviciosRepository.findByServicios(servicioEnum)
                .orElseThrow(() -> new ValidationException("Servicio " + servicioEnum + " no encontrado"));
    }

    /**
     * Obtiene la tarifa de precio vigente activa para el servicio consultado.
     *
     * @param servicio Entidad Servicios a consultar.
     * @return Tarifa_Servicio activa.
     * @throws ValidationException Si no existe tarifa vigente.
     */
    private Tarifa_Servicio obtenerTarifaVigente(Servicios servicio) {
        return precioTarifaRepository.findTarifaVigente(servicio)
                .orElseThrow(() -> new ValidationException("No existe tarifa vigente para el servicio"));
    }

    /**
     * Centraliza el cálculo del consumo promedio ajustado, aplicando factores de corrección
     * por estacionalidad mensual y tamaño/composición demográfica del hogar.
     *
     * @param idHogar      Identificador único del hogar.
     * @param servicioEnum Tipo de servicio (GAS).
     * @param hogar        Entidad Hogar con los datos de sus integrantes.
     * @return BigDecimal con el promedio final ponderado.
     */
    private BigDecimal calcularPromedioFinalAjustado(Long idHogar, ServiciosEnum servicioEnum, Hogar hogar) {
        BigDecimal promedio = facturaRepository.calcularPromedioConsumo(idHogar, servicioEnum);
        if (promedio == null) promedio = BigDecimal.ZERO;

        double factorMes = Estacionalidad.getFactor(LocalDate.now().getMonth());
        BigDecimal promedioAjustado = promedio.multiply(BigDecimal.valueOf(factorMes))
                .setScale(4, RoundingMode.HALF_UP);

        double factorHogar = IntegrantesFamilia.calcularFactorHogar(hogar);
        return promedioAjustado.multiply(BigDecimal.valueOf(factorHogar))
                .setScale(4, RoundingMode.HALF_UP);
    }

    // --- MÉTODOS PRINCIPALES ---



// --- MÉTODOS PRINCIPALES ---

    /**
     * Calcula en tiempo real el consumo diario estimado de gas para el hogar autenticado,
     * basado en las horas transcurridas del día actual en Colombia.
     *
     * @return DTO con el consumo actual del día, el costo base y la tasa de consumo por hora.
     */
    @Override
    public ConsumoDTODiarioGas calcularConsumoActual() {
        Usuarios principal = obtenerUsuarioAutenticado();
        Hogar hogar = obtenerHogarPorUsuario(principal.getIdUsuario());
        Servicios servicioGas = obtenerServicio(ServiciosEnum.GAS);
        Tarifa_Servicio tarifaGas = obtenerTarifaVigente(servicioGas);

        BigDecimal promedioFinal = calcularPromedioFinalAjustado(hogar.getIdHogar(), ServiciosEnum.GAS, hogar);

        ZonedDateTime ahoraColombia = ZonedDateTime.now(ZONA_COLOMBIA);

        int diasMes = ahoraColombia.toLocalDate().lengthOfMonth();
        BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 6, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 6, RoundingMode.HALF_UP);

        int horasTranscurridas = ahoraColombia.getHour();
        BigDecimal consumoActualHoy = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas))
                .setScale(6, RoundingMode.HALF_UP);

        BigDecimal costoBase = consumoActualHoy.multiply(tarifaGas.getPreciounidad())
                .setScale(2, RoundingMode.HALF_UP);

        return new ConsumoDTODiarioGas(consumoActualHoy, costoBase, consumoHora);
    }

    /**
     * Calcula el consumo y costo acumulado de gas desde el inicio del período de facturación actual
     * hasta el momento exacto de la consulta.
     *
     * @return DTO con el consumo acumulado y el costo total asociado.
     */
    @Override
    public ConsumoDTOacomuladoGas obtenerConsumoAcomulado() {
        Usuarios principal = obtenerUsuarioAutenticado();
        Hogar hogar = obtenerHogarPorUsuario(principal.getIdUsuario());
        Servicios servicioGas = obtenerServicio(ServiciosEnum.GAS);
        Tarifa_Servicio tarifaGas = obtenerTarifaVigente(servicioGas);

        BigDecimal promedioFinal = calcularPromedioFinalAjustado(hogar.getIdHogar(), ServiciosEnum.GAS, hogar);

        ZonedDateTime ahoraColombia = ZonedDateTime.now(ZONA_COLOMBIA);
        LocalDateTime ahora = ahoraColombia.toLocalDateTime(); // Extraemos el equivalente local pero con hora de Colombia

        int diasMes = ahora.toLocalDate().lengthOfMonth();
        BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 4, RoundingMode.HALF_UP);

        Optional<Factura> ultimaFacturaOpt = facturaRepository
                .findTopByHogar_IdHogarAndServicios_ServiciosOrderByFechaPeriodoFinDesc(
                        hogar.getIdHogar(), ServiciosEnum.GAS);

        LocalDateTime inicioRecibo;
        if (ultimaFacturaOpt.isPresent() && ultimaFacturaOpt.get().getFechaPeriodoFin() != null) {
            inicioRecibo = ultimaFacturaOpt.get().getFechaPeriodoFin().plusDays(1).atStartOfDay();
        } else {
            LocalDate inicioBase = LocalDate.of(ahora.getYear(), ahora.getMonth(), 25);
            inicioRecibo = inicioBase.atStartOfDay();
            if (ahora.isBefore(inicioRecibo)) {
                inicioRecibo = inicioBase.minusMonths(1).atStartOfDay();
            }
        }

        long horasTranscurridas = ChronoUnit.HOURS.between(inicioRecibo, ahora);
        if (horasTranscurridas < 0) horasTranscurridas = 0;

        BigDecimal consumoAcumulado = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas));
        BigDecimal costoAcumulado = consumoAcumulado.multiply(tarifaGas.getPreciounidad());

        ConsumoDTOacomuladoGas dto = new ConsumoDTOacomuladoGas();
        dto.setConsumoAcomulado(consumoAcumulado);
        dto.setCostoAcomulado(costoAcumulado);

        return dto;
    }

    /**
     * Tarea programada ejecutada diariamente a medianoche para registrar y persistir el histórico
     * del consumo diario de gas de todos los hogares registrados.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void guardarConsumoDiario() {
        ZonedDateTime ahoraColombia = ZonedDateTime.now(ZONA_COLOMBIA);
        System.out.println("🕒 Ejecutando guardarConsumoDiario a las (Colombia): " + ahoraColombia);

        LocalDate hoy = ahoraColombia.toLocalDate().minusDays(1); // El día que terminó oficialmente en Colombia
        List<Hogar> hogares = hogarRepository.findAll();
        Servicios servicioGas = obtenerServicio(ServiciosEnum.GAS);
        Tarifa_Servicio tarifaGas = obtenerTarifaVigente(servicioGas);

        for (Hogar hogar : hogares) {
            BigDecimal promedioFinal = calcularPromedioFinalAjustado(hogar.getIdHogar(), ServiciosEnum.GAS, hogar);

            int diasMes = hoy.lengthOfMonth();
            BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
            BigDecimal costoDiario = consumoDiario.multiply(tarifaGas.getPreciounidad());

            Consumo_Servicio consumo = new Consumo_Servicio();
            consumo.setHogar(hogar);
            consumo.setServicios(servicioGas);
            consumo.setFecha(hoy);
            consumo.setConsumo(consumoDiario);
            consumo.setCosto(costoDiario);
        }
    }

    /**
     * Tarea programada para cerrar y generar de forma automática las facturas mensuales
     * al cumplirse el ciclo de facturación de cada hogar, aplicando factores de estrato socioeconómico.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void cerrarFacturasMensuales() {
        LocalDate hoy = LocalDate.now();
        List<Hogar> hogares = hogarRepository.findAll();
        List<Servicios> servicios = serviciosRepository.findAll();

        for (Hogar hogar : hogares) {
            for (Servicios servicio : servicios) {
                Optional<Factura> ultimaFacturaOpt = facturaRepository.findTopByHogar_IdHogarAndServicios_ServiciosOrderByFechaPeriodoFinDesc(
                        hogar.getIdHogar(), servicio.getServicios());

                if (ultimaFacturaOpt.isEmpty() || ultimaFacturaOpt.get().getFechaPeriodoFin() == null) {
                    continue;
                }

                Factura ultimaFactura = ultimaFacturaOpt.get();
                LocalDate fechaInicio = ultimaFactura.getFechaPeriodoFin();
                LocalDate fechaFinEsperada = fechaInicio.plusMonths(1);

                if (!hoy.equals(fechaFinEsperada)) {
                    continue;
                }

                BigDecimal promedioFinal = calcularPromedioFinalAjustado(hogar.getIdHogar(), servicio.getServicios(), hogar);
                Tarifa_Servicio tarifa = obtenerTarifaVigente(servicio);

                BigDecimal costoTotal = promedioFinal.multiply(tarifa.getPreciounidad());
                BigDecimal costoFinal = costoTotal.setScale(2, RoundingMode.HALF_UP);

                if (servicio.getServicios() == ServiciosEnum.GAS && hogar.getEstratoGas() != null) {
                    Estrato_Gas estrato = hogar.getEstratoGas();
                    BigDecimal factorEstratoBd = BigDecimal.valueOf(1 + estrato.getFactor());
                    costoFinal = costoTotal.multiply(factorEstratoBd).setScale(2, RoundingMode.HALF_UP);
                }

                Factura factura = new Factura();
                factura.setFecha_periodoInicio(fechaInicio);
                factura.setFechaPeriodoFin(fechaFinEsperada);
                factura.setConsumoTotal(promedioFinal);
                factura.setCosto_total(costoFinal);
                factura.setHogar(hogar);
                factura.setServicios(servicio);

                facturaRepository.save(factura);
            }
        }
    }


}
