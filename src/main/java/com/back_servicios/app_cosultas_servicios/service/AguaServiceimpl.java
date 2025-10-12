package com.back_servicios.app_cosultas_servicios.service;

import com.back_servicios.app_cosultas_servicios.domain.dto.response.ConsumoDTODiarioAgua;
import com.back_servicios.app_cosultas_servicios.domain.dto.response.DTOconsumoAcomuladoAgua;
import com.back_servicios.app_cosultas_servicios.domain.entity.*;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Estrato;
import com.back_servicios.app_cosultas_servicios.domain.enumerated.Role;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;



@Service
@AllArgsConstructor
public class AguaServiceimpl implements AguaService {
    private final ConsumoServicioRepository consumoServicioRepository;
    private final HogarRepository hogarRepository;
    private final FacturaRepository facturaRepository;
    private final ServiciosRepository serviciosRepository;
    private final PrecioTarifaRepository precioTarifaRepository;



    @Override
    public ConsumoDTODiarioAgua calcularConsumoActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuarios principal = (Usuarios) auth.getPrincipal();

        Hogar hogar = hogarRepository.findByUsuarioIdUsuario(principal.getIdUsuario())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));

        Servicios servicioAgua = serviciosRepository.findByServicios(ServiciosEnum.AGUA)
                .orElseThrow(() -> new ValidationException("Servicio Agua no encontrado"));


        Tarifa_Servicio tarifaAgua = precioTarifaRepository.findTarifaVigente(servicioAgua)
                .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Agua"));

        // 2. Calcular promedio histórico  como base
        BigDecimal promedio = facturaRepository.calcularPromedioConsumo(
                hogar.getIdHogar(),
                ServiciosEnum.AGUA
        );
        if (promedio == null) promedio = BigDecimal.ZERO;
        System.out.println("el promedio de esas facturas es "+ promedio);


        // 2) Factor por mes (estacionalidad)
        Month mesActual = LocalDate.now().getMonth();
        double factorMes = Estacionalidad.getFactor(mesActual);
        BigDecimal factorMesBd = BigDecimal.valueOf(factorMes);


        BigDecimal promedioAjustado = promedio.multiply(factorMesBd)
                .setScale(4, RoundingMode.HALF_UP);


        // 4️⃣ Ajuste por número y tipo de integrantes del hogar
        double factorHogar = IntegrantesFamilia.calcularFactorHogar(hogar);
        BigDecimal factorHogarBd = BigDecimal.valueOf(factorHogar);

        BigDecimal promedioFinal = promedioAjustado.multiply(factorHogarBd)
                .setScale(4, RoundingMode.HALF_UP);




        // 5️⃣ Derivar consumo diario y por hora
        int diasMes = LocalDate.now().lengthOfMonth();
        BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 6, RoundingMode.HALF_UP);
        BigDecimal consumoHora = consumoDiario.divide(BigDecimal.valueOf(24), 6, RoundingMode.HALF_UP);

        // 6️⃣ Consumo acumulado del día
        int horasTranscurridas = LocalDateTime.now().getHour();
        BigDecimal consumoActualHoy = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas))
                .setScale(6, RoundingMode.HALF_UP);

        // 7️⃣ Costo base
        BigDecimal precioUnitario = tarifaAgua.getPreciounidad();
        BigDecimal costoBase = consumoActualHoy.multiply(precioUnitario)
                .setScale(2, RoundingMode.HALF_UP);

        // 8️⃣ Ajuste por estrato
        //Estrato estrato = hogar.getEstrato();
        //BigDecimal factorEstratoBd = BigDecimal.valueOf(1 + estrato.getFactor());

       // BigDecimal costoFinal = costoBase.multiply(factorEstratoBd)
         //       .setScale(2, RoundingMode.HALF_UP);

        // 9️⃣ Retornar DTO
        return new ConsumoDTODiarioAgua(
                consumoActualHoy,
                costoBase,
                consumoHora
        );
    }

    @Override
    public DTOconsumoAcomuladoAgua obtenerConsumoAcomulado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuarios principal = (Usuarios) auth.getPrincipal();
        Hogar hogar = hogarRepository.findByUsuarioIdUsuario(principal.getIdUsuario())
                .orElseThrow(() -> new ValidationException("Hogar no encontrado"));

        Servicios servicioAgua = serviciosRepository.findByServicios(ServiciosEnum.AGUA)
                .orElseThrow(() -> new ValidationException("Servicio Agua no encontrado"));


        Tarifa_Servicio tarifaAgua = precioTarifaRepository.findTarifaVigente(servicioAgua)
                .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Agua"));


// 1. Calcular promedio histórico de facturas
        BigDecimal promedio = facturaRepository.calcularPromedioConsumo(
                hogar.getIdHogar(),
                ServiciosEnum.AGUA
        );
        if (promedio == null) promedio = BigDecimal.ZERO;


        Month mesActual = LocalDate.now().getMonth();
        double factorMes = Estacionalidad.getFactor(mesActual);   // ej. 1.20, 0.95, etc.
        BigDecimal factorMesBd = BigDecimal.valueOf(factorMes);


        BigDecimal promedioAjustado = promedio.multiply(factorMesBd)
                .setScale(4, RoundingMode.HALF_UP);


        // 4️⃣ Ajuste por número y tipo de integrantes del hogar
        double factorHogar = IntegrantesFamilia.calcularFactorHogar(hogar);
        BigDecimal factorHogarBd = BigDecimal.valueOf(factorHogar);

        BigDecimal promedioFinal = promedioAjustado.multiply(factorHogarBd)
                .setScale(4, RoundingMode.HALF_UP);

        // 2. Derivar consumo por hora
        int diasMes = LocalDate.now().lengthOfMonth();
        BigDecimal consumoDiario = promedioFinal.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
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

        BigDecimal precioUnitario = tarifaAgua.getPreciounidad();
        // 5. Calcular acumulado
        BigDecimal consumoAcumulado = consumoHora.multiply(BigDecimal.valueOf(horasTranscurridas));
        BigDecimal costoAcumulado = consumoAcumulado.multiply(precioUnitario);

        // 6. Retornar DTO
        DTOconsumoAcomuladoAgua dto = new DTOconsumoAcomuladoAgua();
        dto.setConsumoAcomulado(consumoAcumulado);
        dto.setCostoAcomulado(costoAcumulado);

        return dto;
    }




    @Scheduled(cron = "0 0 0 * * *") // todos los días a medianoche
    public void guardarConsumoDiario() {
        System.out.println("🕒 Ejecutando guardarConsumoDiario a las: " + LocalDateTime.now());
        LocalDate hoy = LocalDate.now().minusDays(1); // día que terminó
        List<Hogar> hogares = hogarRepository.findAll();

        for (Hogar hogar : hogares) {
            BigDecimal promedio = facturaRepository.calcularPromedioConsumo(
                    hogar.getIdHogar(),
                    ServiciosEnum.AGUA
            );
            if (promedio == null) promedio = BigDecimal.ZERO;
            System.out.println("Promedio hogar " + hogar.getIdHogar() + ": " + promedio);

            Servicios servicioAgua = serviciosRepository.findByServicios(ServiciosEnum.AGUA)
                    .orElseThrow(() -> new ValidationException("Servicio Agua no encontrado"));

            Tarifa_Servicio tarifaAgua = precioTarifaRepository.findTarifaVigente(servicioAgua)
                    .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Agua"));

            int diasMes = hoy.lengthOfMonth();
            BigDecimal consumoDiario = promedio.divide(BigDecimal.valueOf(diasMes), 4, RoundingMode.HALF_UP);
            BigDecimal precioUnitario = tarifaAgua.getPreciounidad();
            BigDecimal costoDiario = consumoDiario.multiply(precioUnitario);



            Consumo_Servicio consumo = new Consumo_Servicio();
            consumo.setHogar(hogar);
            consumo.setServicios(servicioAgua);
            consumo.setFecha(hoy);
            consumo.setConsumo(consumoDiario);
            consumo.setCosto(costoDiario);

            try {
                consumoServicioRepository.save(consumo);
                System.out.println("✅ Consumo guardado para hogar " + hogar.getIdHogar());
            }catch (Exception e) {
                e.printStackTrace();
            }

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
                    BigDecimal consumoPromedio = facturaRepository.calcularPromedioConsumo(
                            hogar.getIdHogar(),
                            ServiciosEnum.AGUA);
                    if (consumoPromedio == null) consumoPromedio = BigDecimal.ZERO;// aquí usas tu lógica real
                    BigDecimal consumoTotal = consumoPromedio.multiply(BigDecimal.valueOf(30));

                    Servicios servicioAgua = serviciosRepository.findByServicios(ServiciosEnum.AGUA)
                            .orElseThrow(() -> new ValidationException("Servicio Agua no encontrado"));

                    Tarifa_Servicio tarifaAgua = precioTarifaRepository.findTarifaVigente(servicioAgua)
                            .orElseThrow(() -> new ValidationException("No existe tarifa vigente para Agua"));

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