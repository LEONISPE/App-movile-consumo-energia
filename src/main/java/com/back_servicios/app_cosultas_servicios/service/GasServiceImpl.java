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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GasServiceImpl implements GasService {

    private  final ServiciosRepository serviciosRepository;
    private  final HogarRepository hogarRepository;
    private final PrecioTarifaRepository precioTarifaRepository;
    private  final FacturaRepository facturaRepository;


    @Override
    public ConsumoDTODiarioGas calcularConsumoActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuarios principal = (Usuarios) auth.getPrincipal();

        Hogar hogar = hogarRepository.findByUsuarios_IdUsuario(principal.getIdUsuario())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));

        Servicios servicioGas = serviciosRepository.findByServicios(ServiciosEnum.GAS)
                .orElseThrow(() -> new ValidationException("Servicio Gas no encontrado"));


        Tarifa_Servicio tarifaGas = precioTarifaRepository.findTarifaVigente(servicioGas)
                .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Gas"));


        BigDecimal promedio = facturaRepository.calcularPromedioConsumo(
                hogar.getIdHogar(),
                ServiciosEnum.GAS
        );
        if (promedio == null) promedio = BigDecimal.ZERO;
        System.out.println("el promedio de esas facturas es "+ promedio);



        Month mesActual = LocalDate.now().getMonth();
        double factorMes = Estacionalidad.getFactor(mesActual);
        BigDecimal factorMesBd = BigDecimal.valueOf(factorMes);


        BigDecimal promedioAjustado = promedio.multiply(factorMesBd)
                .setScale(4, RoundingMode.HALF_UP);



        double factorHogar = IntegrantesFamilia.calcularFactorHogar(hogar);
        BigDecimal factorHogarBd = BigDecimal.valueOf(factorHogar);

        BigDecimal promedioFinal = promedioAjustado.multiply(factorHogarBd)
                .setScale(4, RoundingMode.HALF_UP);





        int diasMes = LocalDate.now().lengthOfMonth();
        BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 6, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 6, RoundingMode.HALF_UP);


        int horasTranscurridas = LocalDateTime.now().getHour();
        BigDecimal consumoActualHoy = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas))
                .setScale(6, RoundingMode.HALF_UP);


        BigDecimal precioUnitario = tarifaGas.getPreciounidad();
        BigDecimal costoBase = consumoActualHoy.multiply(precioUnitario)
                .setScale(2, RoundingMode.HALF_UP);




        return new ConsumoDTODiarioGas(
                consumoActualHoy,
                costoBase,
                consumoHora
        );
    }
    @Override
    public ConsumoDTOacomuladoGas obtenerConsumoAcomulado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuarios principal = (Usuarios) auth.getPrincipal();
        Hogar hogar = hogarRepository.findByUsuarios_IdUsuario(principal.getIdUsuario())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));

        Servicios servicioGas = serviciosRepository.findByServicios(ServiciosEnum.GAS)
                .orElseThrow(() -> new ValidationException("Servicio Gas no encontrado"));


        Tarifa_Servicio tarifaGas = precioTarifaRepository.findTarifaVigente(servicioGas)
                .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Gas"));



        BigDecimal promedio = facturaRepository.calcularPromedioConsumo(
                hogar.getIdHogar(),
                ServiciosEnum.GAS
        );
        if (promedio == null) promedio = BigDecimal.ZERO;


        Month mesActual = LocalDate.now().getMonth();
        double factorMes = Estacionalidad.getFactor(mesActual);
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


        Optional<Factura> ultimaFacturaOpt = facturaRepository
                .findTopByHogar_IdHogarAndServicios_ServiciosOrderByFechaPeriodoFinDesc(
                        hogar.getIdHogar(), ServiciosEnum.GAS);


        LocalDateTime inicioRecibo;
        if (ultimaFacturaOpt.isPresent()) {
            Factura ultimaFactura = ultimaFacturaOpt.get();


            LocalDate fechaFinAnterior = ultimaFactura.getFechaPeriodoFin();
            if (fechaFinAnterior == null) {
                LocalDateTime ahora = LocalDateTime.now();
                LocalDate inicioBase = LocalDate.of(ahora.getYear(), ahora.getMonth(), 25);
                inicioRecibo = inicioBase.atStartOfDay();
                if (ahora.isBefore(inicioRecibo)) inicioRecibo = inicioBase.minusMonths(1).atStartOfDay();
            } else {

                inicioRecibo = fechaFinAnterior.plusDays(1).atStartOfDay();
            }
        } else {
            LocalDateTime ahora = LocalDateTime.now();
            LocalDate inicioBase = LocalDate.of(ahora.getYear(), ahora.getMonth(), 25);
            inicioRecibo = inicioBase.atStartOfDay();
            if (ahora.isBefore(inicioRecibo)) {
                inicioRecibo = inicioBase.minusMonths(1).atStartOfDay();
            }
        }

        LocalDateTime ahora = LocalDateTime.now();
        long horasTranscurridas = ChronoUnit.HOURS.between(inicioRecibo, ahora);
        if (horasTranscurridas < 0) horasTranscurridas = 0;


        BigDecimal precioUnitario = tarifaGas.getPreciounidad();
        BigDecimal consumoAcumulado = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas));
        BigDecimal costoAcumulado = consumoAcumulado.multiply(precioUnitario);


        ConsumoDTOacomuladoGas dto = new ConsumoDTOacomuladoGas();
        dto.setConsumoAcomulado(consumoAcumulado);
        dto.setCostoAcomulado(costoAcumulado);

        return dto;
    }




    @Scheduled(cron = "0 0 0 * * *")
    public void guardarConsumoDiario() {
        System.out.println("🕒 Ejecutando guardarConsumoDiario a las: " + LocalDateTime.now());
        LocalDate hoy = LocalDate.now().minusDays(1); // día que terminó
        List<Hogar> hogares = hogarRepository.findAll();

        for (Hogar hogar : hogares) {
            BigDecimal promedio = facturaRepository.calcularPromedioConsumo(
                    hogar.getIdHogar(),
                    ServiciosEnum.GAS
            );
            if (promedio == null) promedio = BigDecimal.ZERO;
            System.out.println("Promedio hogar " + hogar.getIdHogar() + ": " + promedio);

            Servicios servicioGas = serviciosRepository.findByServicios(ServiciosEnum.GAS)
                    .orElseThrow(() -> new ValidationException("Servicio Gas no encontrado"));

            Tarifa_Servicio tarifaGas = precioTarifaRepository.findTarifaVigente(servicioGas)
                    .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Gas"));

            Month mesActual = LocalDate.now().getMonth();
            double factorMes = Estacionalidad.getFactor(mesActual);
            BigDecimal factorMesBd = BigDecimal.valueOf(factorMes);


            BigDecimal promedioAjustado = promedio.multiply(factorMesBd)
                    .setScale(4, RoundingMode.HALF_UP);



            double factorHogar = IntegrantesFamilia.calcularFactorHogar(hogar);
            BigDecimal factorHogarBd = BigDecimal.valueOf(factorHogar);

            BigDecimal promedioFinal = promedioAjustado.multiply(factorHogarBd)
                    .setScale(4, RoundingMode.HALF_UP);

            int diasMes = hoy.lengthOfMonth();
            BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
            BigDecimal precioUnitario = tarifaGas.getPreciounidad();
            BigDecimal costoDiario = consumoDiario.multiply(precioUnitario);


            Consumo_Servicio consumo = new Consumo_Servicio();
            consumo.setHogar(hogar);
            consumo.setServicios(servicioGas);
            consumo.setFecha(hoy);
            consumo.setConsumo(consumoDiario);
            consumo.setCosto(costoDiario);

        }
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void cerrarFacturasMensuales() {
        LocalDate hoy = LocalDate.now();

        List<Hogar> hogares = hogarRepository.findAll();
        List<Servicios> servicios = serviciosRepository.findAll();

        for (Hogar hogar : hogares) {
            for (Servicios servicio : servicios) {


                Optional<Factura> ultimaFacturaOpt = facturaRepository.findTopByHogar_IdHogarAndServicios_ServiciosOrderByFechaPeriodoFinDesc(
                        hogar.getIdHogar(), servicio.getServicios());

                if (ultimaFacturaOpt.isEmpty()) {
                    System.out.println("No hay facturas previas para hogar " + hogar.getIdHogar() + " - servicio " + servicio.getServicios());
                    continue;
                }

                Factura ultimaFactura = ultimaFacturaOpt.get();
                LocalDate fechaInicio = ultimaFactura.getFechaPeriodoFin();
                LocalDate fechaFinEsperada = fechaInicio.plusMonths(1);


                if (!hoy.equals(fechaFinEsperada)) {
                    continue;
                }

                System.out.println("🔒 Cerrando factura para hogar " + hogar.getIdHogar() + " - servicio " + servicio.getServicios());
                BigDecimal consumoPromedio = facturaRepository.calcularPromedioConsumo(
                        hogar.getIdHogar(),
                        ServiciosEnum.GAS);
                if (consumoPromedio == null) consumoPromedio = BigDecimal.ZERO;




                Month mesActual = LocalDate.now().getMonth();
                double factorMes = Estacionalidad.getFactor(mesActual);
                BigDecimal factorMesBd = BigDecimal.valueOf(factorMes);


                BigDecimal promedioAjustado = consumoPromedio.multiply(factorMesBd)
                        .setScale(4, RoundingMode.HALF_UP);



                double factorHogar = IntegrantesFamilia.calcularFactorHogar(hogar);
                BigDecimal factorHogarBd = BigDecimal.valueOf(factorHogar);

                BigDecimal promedioFinal = promedioAjustado.multiply(factorHogarBd)
                        .setScale(4, RoundingMode.HALF_UP);


                Servicios servicioGas = serviciosRepository.findByServicios(ServiciosEnum.GAS)
                        .orElseThrow(() -> new ValidationException("Servicio Gas no encontrado"));

                Tarifa_Servicio tarifaGas = precioTarifaRepository.findTarifaVigente(servicioGas)
                        .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Gas"));

                BigDecimal precioUnitario = tarifaGas.getPreciounidad();
                BigDecimal costoTotal = promedioFinal.multiply(precioUnitario);



                Estrato_Gas estrato = hogar.getEstratoGas();
                BigDecimal factorEstratoBd = BigDecimal.valueOf(1 + estrato.getFactor());

                BigDecimal costoFinal = costoTotal.multiply(factorEstratoBd)
                        .setScale(2, RoundingMode.HALF_UP);




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
