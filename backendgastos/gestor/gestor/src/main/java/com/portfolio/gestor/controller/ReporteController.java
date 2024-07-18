package com.portfolio.gestor.controller;


import com.portfolio.gestor.DTO.ReporteEstadoCuentaDTO;
import com.portfolio.gestor.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<ReporteEstadoCuentaDTO>> generarReporteEstadoCuenta(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate fechaFin,
            @RequestParam Long clienteId){
        List<ReporteEstadoCuentaDTO> reporte = reporteService.generarReporteEstadoCuenta(fechaInicio, fechaFin, clienteId);
        return new ResponseEntity<>(reporte, HttpStatus.OK);
    }

}
