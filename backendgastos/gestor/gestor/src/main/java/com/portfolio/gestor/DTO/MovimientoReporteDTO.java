package com.portfolio.gestor.DTO;

import java.time.LocalDate;

public class MovimientoReporteDTO {
    private LocalDate fecha;
    private String tipoMovimiento;
    private Double valor;
    private Double saldoDisponible;

    public MovimientoReporteDTO() {
    }

    public MovimientoReporteDTO(LocalDate fecha, String tipoMovimiento, Double valor, Double saldoDisponible) {
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldoDisponible = saldoDisponible;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(Double saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }
}
