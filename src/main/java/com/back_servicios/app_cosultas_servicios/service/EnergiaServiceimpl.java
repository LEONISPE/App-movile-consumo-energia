package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioEnergia;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOConsumoAcomuladoEnergia;
import com.back_servicios.app_cosultas_servicios.domain.entity.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class EnergiaServiceimpl  implements EnergiaService{

    private final HogarRepository hogarRepository;
    private final ServiciosRepository serviciosRepository;
    private final PrecioTarifaRepository precioTarifaRepository;
    private final FacturaRepository facturaRepository;


    @Override
    public ConsumoDTODiarioEnergia calcularConsumoDiarioEnergia(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuarios principal = (Usuarios) auth.getPrincipal();

        Hogar hogar = hogarRepository.findByUsuarioIdUsuario(principal.getIdUsuario())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));

        Servicios servicioEnergia = serviciosRepository.findByServicios(ServiciosEnum.ENERGIA)
                .orElseThrow(() -> new ValidationException("Servicio de energia no encontrado"));


        Tarifa_Servicio tarifaEnergia = precioTarifaRepository.findTarifaVigente(servicioEnergia)
                .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Energia"));

        BigDecimal promedio = facturaRepository.calcularPromedioConsumo(
                hogar.getIdHogar(),
                ServiciosEnum.ENERGIA);
        if (promedio == null) promedio = BigDecimal.ZERO;

        Month mesActual = LocalDate.now().getMonth();
        double factorMes = Estacionalidad.getFactor(mesActual);   // ej. 1.20, 0.95, etc.
        BigDecimal factorMesBd = BigDecimal.valueOf(factorMes);


        BigDecimal promedioAjustado = promedio.multiply(factorMesBd)
                .setScale(4, RoundingMode.HALF_UP);

        double factorHogar = IntegrantesFamilia.calcularFactorHogar(hogar);
        BigDecimal factorHogarBd = BigDecimal.valueOf(factorHogar);

        BigDecimal promedioFinal = promedioAjustado.multiply(factorHogarBd)
                .setScale(4, RoundingMode.HALF_UP);

        int diasMes = LocalDate.now().lengthOfMonth();
        BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 4, RoundingMode.HALF_UP);

        int horasTranscurridas = LocalDateTime.now().getHour(); // 0-23
        BigDecimal consumoActualHoy = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas));

        BigDecimal precioUnitario = tarifaEnergia.getPreciounidad();

        // 5. Calcular costo
        BigDecimal costoActualHoy = consumoActualHoy.multiply(precioUnitario);

return new ConsumoDTODiarioEnergia(
        consumoActualHoy,
        costoActualHoy,
        consumoHora
);
    }

@Override
    public DTOConsumoAcomuladoEnergia ObtenerConsumoAcomuladoEnergia(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuarios principal = (Usuarios) auth.getPrincipal();

        Hogar hogar = hogarRepository.findByUsuarioIdUsuario(principal.getIdUsuario())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));

        Servicios servicioEnergia = serviciosRepository.findByServicios(ServiciosEnum.AGUA)
                .orElseThrow(() -> new ValidationException("Servicio de energia no encontrado"));


        Tarifa_Servicio tarifaAgua = precioTarifaRepository.findTarifaVigente(servicioEnergia)
                .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Energia"));

        BigDecimal promedio = facturaRepository.calcularPromedioConsumo(
                hogar.getIdHogar(),
                ServiciosEnum.ENERGIA);
        if (promedio == null) promedio = BigDecimal.ZERO;

    Month mesActual = LocalDate.now().getMonth();
    double factorMes = Estacionalidad.getFactor(mesActual);   // ej. 1.20, 0.95, etc.
    BigDecimal factorMesBd = BigDecimal.valueOf(factorMes);


    BigDecimal promedioAjustado = promedio.multiply(factorMesBd)
            .setScale(4, RoundingMode.HALF_UP);

    double factorHogar = IntegrantesFamilia.calcularFactorHogar(hogar);
    BigDecimal factorHogarBd = BigDecimal.valueOf(factorHogar);

    BigDecimal promedioFinal = promedioAjustado.multiply(factorHogarBd)
            .setScale(4, RoundingMode.HALF_UP);

        int diasMes = LocalDate.now().lengthOfMonth();
        BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 4, RoundingMode.HALF_UP);

        LocalDateTime ahora = LocalDateTime.now();
        LocalDate inicioRecibo = LocalDate.of(ahora.getYear(), ahora.getMonth(), 25);
        LocalDateTime inicioReciboMesActual = inicioRecibo.atStartOfDay();
        if (ahora.isBefore(inicioReciboMesActual)) {
            inicioReciboMesActual = inicioRecibo.minusMonths(1).atStartOfDay();
        }
        // 4. Calcular horas transcurridas desde inicio del recibo
        long horasTranscurridas = ChronoUnit.HOURS.between(inicioReciboMesActual, ahora);

        BigDecimal precioUnitario = tarifaAgua.getPreciounidad();
        // 5. Calcular acumulado
        BigDecimal consumoAcumulado = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas));
        BigDecimal costoAcumulado = consumoAcumulado.multiply(precioUnitario);

DTOConsumoAcomuladoEnergia dto = new DTOConsumoAcomuladoEnergia();
dto.setConsumoAcomulado(consumoAcumulado);
dto.setCostoAcomulado(costoAcumulado);

return dto;
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void cerrarFacturasMensuales() {
        LocalDate hoy = LocalDate.now();

        // Ejemplo: verificamos si hoy es día 25
        if (hoy.getDayOfMonth() == 26) {
            // Aquí recorres los hogares y servicios
            for (Hogar hogar : hogarRepository.findAll()) {
                for (Servicios servicio : serviciosRepository.findAll()) {

                    // Definir periodo de la factura que acaba hoy
                    LocalDate fechaInicio = hoy.minusMonths(1); // arranca el 25 del mes pasado
                    LocalDate fechaFin = hoy;                   // termina hoy

                    // Calcular consumo total (ejemplo: promedio * 30 días)
                    BigDecimal consumoPromedio = facturaRepository.calcularPromedioConsumo(
                            hogar.getIdHogar(),
                            ServiciosEnum.ENERGIA);
                    if (consumoPromedio == null) consumoPromedio = BigDecimal.ZERO;
                    int diasMes = LocalDate.now().lengthOfMonth();
                    BigDecimal consumoTotal = consumoPromedio.multiply(BigDecimal.valueOf(diasMes));

                    Servicios servicioEnergia = serviciosRepository.findByServicios(ServiciosEnum.ENERGIA)
                            .orElseThrow(() -> new ValidationException("Servicio de energia no encontrado"));

                    Tarifa_Servicio tarifaAgua = precioTarifaRepository.findTarifaVigente(servicioEnergia)
                            .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Energia"));

                    BigDecimal precioUnitario = tarifaAgua.getPreciounidad();
                    // Calcular costo total
                    BigDecimal costoTotal = consumoTotal.multiply(precioUnitario);

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