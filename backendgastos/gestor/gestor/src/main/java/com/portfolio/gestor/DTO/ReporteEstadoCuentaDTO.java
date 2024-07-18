package com.portfolio.gestor.DTO;

import java.util.List;

public class ReporteEstadoCuentaDTO {
    private String nombre;
    private String tipoCuenta;
    private String numeroCuenta;
    private Double saldoActual;
    private List<MovimientoReporteDTO> movimientos;

    public ReporteEstadoCuentaDTO(String nombre, String tipoCuenta, String numeroCuenta, Double saldoActual, List<MovimientoReporteDTO> movimientos) {
        this.nombre = nombre;
        this.tipoCuenta = tipoCuenta;
        this.numeroCuenta = numeroCuenta;
        this.saldoActual = saldoActual;
        this.movimientos = movimientos;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public ReporteEstadoCuentaDTO() {
    }

    public ReporteEstadoCuentaDTO(String numeroCuenta, Double saldoActual, List<MovimientoReporteDTO> movimientos) {
        this.numeroCuenta = numeroCuenta;
        this.saldoActual = saldoActual;
        this.movimientos = movimientos;
    }

    public ReporteEstadoCuentaDTO(String nombre, String numeroCuenta, Double saldoActual, List<MovimientoReporteDTO> movimientos) {
        this.nombre = nombre;
        this.numeroCuenta = numeroCuenta;
        this.saldoActual = saldoActual;
        this.movimientos = movimientos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(Double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public List<MovimientoReporteDTO> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoReporteDTO> movimientos) {
        this.movimientos = movimientos;
    }
}


