package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioAgua;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioEnergia;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOConsumoAcomuladoEnergia;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOconsumoAcomuladoAgua;
import com.back_servicios.app_cosultas_servicios.domain.entity.*;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Agua;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato_Energia;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.ServiciosEnum;
import com.back_servicios.app_cosultas_servicios.exceptions.ValidationException;
import com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos.Estacionalidad;
import com.back_servicios.app_cosultas_servicios.infra.FactoresConsumos.IntegrantesFamilia;
import com.back_servicios.app_cosultas_servicios.repository.*;
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
 * <h1>Implementación del servicio de cálculo y gestión del Agua</h1>
 * <p>
 * Clase encargada de procesar el consumo diario, actual y acumulado del servicio de agua potable,
 * integrando estacionalidad mensual, tamaño del hogar y tarifas vigentes bajo la zona horaria de Colombia.
 * </p>
 *
 * @author SQA Team
 * @version 1.0
 */


@Service
@AllArgsConstructor
public class AguaServiceimpl implements AguaService {
    private final ConsumoServicioRepository consumoServicioRepository;
    private final HogarRepository hogarRepository;
    private final FacturaRepository facturaRepository;
    private final ServiciosRepository serviciosRepository;
    private final PrecioTarifaRepository precioTarifaRepository;

    private static final ZoneId ZONA_COLOMBIA = ZoneId.of("America/Bogota");

    // --- MÉTODOS AUXILIARES PRIVADOS (Principio DRY) ---

    /**
     * Obtiene el usuario autenticado actualmente en la sesión de Spring Security.
     *
     * @return Entidad Usuarios autenticada.
     */
    private Usuarios obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuarios) auth.getPrincipal();
    }

    /**
     * Obtiene el hogar asociado al usuario autenticado.
     *
     * @param idUsuario Identificador único del usuario.
     * @return Entidad Hogar vinculada.
     * @throws ValidationException Si el hogar no es encontrado.
     */
    private Hogar obtenerHogarPorUsuario(Long idUsuario) {
        return hogarRepository.findByUsuarios_IdUsuario(idUsuario)
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));
    }

    /**
     * Obtiene el servicio de agua del repositorio.
     *
     * @param servicioEnum Tipo de servicio (AGUA).
     * @return Entidad Servicios correspondiente.
     * @throws ValidationException Si el servicio no existe.
     */
    private Servicios obtenerServicio(ServiciosEnum servicioEnum) {
        return serviciosRepository.findByServicios(servicioEnum)
                .orElseThrow(() -> new ValidationException("Servicio " + servicioEnum + " no encontrado"));
    }

    /**
     * Obtiene la tarifa de precio vigente para el servicio de agua.
     *
     * @param servicio Entidad Servicios de agua.
     * @return Tarifa_Servicio activa.
     * @throws ValidationException Si no existe tarifa vigente.
     */
    private Tarifa_Servicio obtenerTarifaVigente(Servicios servicio) {
        return precioTarifaRepository.findTarifaVigente(servicio)
                .orElseThrow(() -> new ValidationException("No existe tarifa vigente para el servicio"));
    }

    /**
     * Centraliza el cálculo del promedio ajustado por estacionalidad y tamaño del hogar.
     */
    /**
            * Centraliza el cálculo del promedio ajustado por estacionalidad mensual y factor familiar del hogar.
            *
            * @param idHogar      Identificador del hogar.
            * @param servicioEnum Tipo de servicio (AGUA).
            * @param hogar        Entidad Hogar con sus integrantes.
            * @return BigDecimal con el promedio ponderado de consumo de agua.
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



    /**
     * Calcula en tiempo real el consumo diario de agua potable del hogar en curso,
     * considerando las horas transcurridas en el día.
     *
     * @return DTO con el consumo actual de agua, costo base y tasa por hora.
     */
    @Override
    public ConsumoDTODiarioAgua calcularConsumoActual() {
        Usuarios principal = obtenerUsuarioAutenticado();
        Hogar hogar = obtenerHogarPorUsuario(principal.getIdUsuario());
        Servicios servicioAgua = obtenerServicio(ServiciosEnum.AGUA);
        Tarifa_Servicio tarifaAgua = obtenerTarifaVigente(servicioAgua);

        BigDecimal promedioFinal = calcularPromedioFinalAjustado(hogar.getIdHogar(), ServiciosEnum.AGUA, hogar);

        ZonedDateTime ahoraColombia = ZonedDateTime.now(ZONA_COLOMBIA);

        int diasMes = ahoraColombia.toLocalDate().lengthOfMonth();
        BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 6, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 6, RoundingMode.HALF_UP);

        int horasTranscurridas = ahoraColombia.getHour();
        BigDecimal consumoActualHoy = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas))
                .setScale(6, RoundingMode.HALF_UP);

        BigDecimal costoBase = consumoActualHoy.multiply(tarifaAgua.getPreciounidad())
                .setScale(2, RoundingMode.HALF_UP);



        return new ConsumoDTODiarioAgua(consumoActualHoy, costoBase, consumoHora);
    }

    /**
     * Calcula el consumo y costo acumulado de agua potable desde el inicio del período de facturación actual.
     *
     * @return DTO con el consumo y costo acumulado de agua.
     */
    @Override
    public DTOconsumoAcomuladoAgua obtenerConsumoAcomulado() {
        Usuarios principal = obtenerUsuarioAutenticado();
        Hogar hogar = obtenerHogarPorUsuario(principal.getIdUsuario());
        Servicios servicioAgua = obtenerServicio(ServiciosEnum.AGUA);
        Tarifa_Servicio tarifaAgua = obtenerTarifaVigente(servicioAgua);

        BigDecimal promedioFinal = calcularPromedioFinalAjustado(hogar.getIdHogar(), ServiciosEnum.AGUA, hogar);

        ZonedDateTime ahoraColombia = ZonedDateTime.now(ZONA_COLOMBIA);
        LocalDateTime ahora = ahoraColombia.toLocalDateTime(); // Extraemos el equivalente local pero con hora de Colombia

        int diasMes = ahora.toLocalDate().lengthOfMonth();
        BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 4, RoundingMode.HALF_UP);

        Optional<Factura> ultimaFacturaOpt = facturaRepository
                .findTopByHogar_IdHogarAndServicios_ServiciosOrderByFechaPeriodoFinDesc(
                        hogar.getIdHogar(), ServiciosEnum.AGUA);

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
        BigDecimal costoAcumulado = consumoAcumulado.multiply(tarifaAgua.getPreciounidad());

        DTOconsumoAcomuladoAgua dto = new DTOconsumoAcomuladoAgua();
        dto.setConsumoAcomulado(consumoAcumulado);
        dto.setCostoAcomulado(costoAcumulado);

        return dto;
    }



    /**
     * Tarea programada diaria para registrar y persistir el consumo diario de agua de todos los hogares.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void guardarConsumoDiario() {
        ZonedDateTime ahoraColombia = ZonedDateTime.now(ZONA_COLOMBIA);
        System.out.println("🕒 Ejecutando guardarConsumoDiario a las (Colombia): " + ahoraColombia);

        LocalDate hoy = ahoraColombia.toLocalDate().minusDays(1); // El día que terminó oficialmente en Colombia
        List<Hogar> hogares = hogarRepository.findAll();
        Servicios servicioAgua = obtenerServicio(ServiciosEnum.AGUA);
        Tarifa_Servicio tarifaAgua = obtenerTarifaVigente(servicioAgua);

        for (Hogar hogar : hogares) {
            BigDecimal promedioFinal = calcularPromedioFinalAjustado(hogar.getIdHogar(), ServiciosEnum.AGUA, hogar);

            int diasMes = hoy.lengthOfMonth();
            BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
            BigDecimal costoDiario = consumoDiario.multiply(tarifaAgua.getPreciounidad());

            Consumo_Servicio consumo = new Consumo_Servicio();
            consumo.setHogar(hogar);
            consumo.setServicios(servicioAgua);
            consumo.setFecha(hoy);
            consumo.setConsumo(consumoDiario);
            consumo.setCosto(costoDiario);
        }
    }

    /**
     * Tarea programada para cerrar facturas mensuales de agua al cumplirse el ciclo de cada hogar,
     * aplicando el factor de estrato de agua correspondiente.
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

                if (servicio.getServicios() == ServiciosEnum.AGUA && hogar.getEstratoAgua() != null) {
                    Estrato_Agua estrato = hogar.getEstratoAgua();
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
