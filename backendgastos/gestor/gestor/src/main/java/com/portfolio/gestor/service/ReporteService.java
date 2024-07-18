package com.portfolio.gestor.service;

import com.portfolio.gestor.DTO.MovimientoReporteDTO;
import com.portfolio.gestor.DTO.ReporteEstadoCuentaDTO;
import com.portfolio.gestor.entity.Cuenta;
import com.portfolio.gestor.entity.Movimiento;
import com.portfolio.gestor.repository.CuentaRepository;
import com.portfolio.gestor.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    public List<ReporteEstadoCuentaDTO> generarReporteEstadoCuenta(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId){
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        return cuentas.stream().map(cuenta -> {
            List<Movimiento> movimientos = movimientoRepository.findByFechaBetweenAndCuentaId(fechaInicio, fechaFin, cuenta.getId());

            List<MovimientoReporteDTO> movimientosDTO = movimientos.stream().map(movimiento -> {
                MovimientoReporteDTO movimientoDTO = new MovimientoReporteDTO();
                movimientoDTO.setFecha(movimiento.getFecha());
                movimientoDTO.setTipoMovimiento(movimiento.getTipoMovimiento());
                movimientoDTO.setValor(movimiento.getValor());
                movimientoDTO.setSaldoDisponible(movimiento.getSaldo());
                return movimientoDTO;
            }).collect(Collectors.toList());

            ReporteEstadoCuentaDTO reporteDTO = new ReporteEstadoCuentaDTO();
            reporteDTO.setNombre(cuenta.getCliente().getPersona().getNombre());
            reporteDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
            reporteDTO.setTipoCuenta(cuenta.getTipoCuenta());
            reporteDTO.setSaldoActual(cuenta.getSaldoInicial());
            reporteDTO.setMovimientos(movimientosDTO);
            return reporteDTO;
        }).collect(Collectors.toList());
    }

}
