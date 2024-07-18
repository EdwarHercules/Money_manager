package com.portfolio.gestor.controller;

import com.portfolio.gestor.DTO.MovimientoDTO;
import com.portfolio.gestor.DTO.MovimientoListadoDTO;
import com.portfolio.gestor.DTO.MovimientoObjectDTO;
import com.portfolio.gestor.entity.Movimiento;
import com.portfolio.gestor.exceptions.CuentaNoEncontradaException;
import com.portfolio.gestor.exceptions.SaldoInsuficienteException;
import com.portfolio.gestor.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    // Endpoint para obtener todos los movimientos
    @GetMapping
    public ResponseEntity<List<MovimientoListadoDTO>> obtenerTodosMovimientos(){
        List<MovimientoListadoDTO> movimientos = movimientoService.obtenerTodosMovimientos();
        return new ResponseEntity<>(movimientos, HttpStatus.OK);
    }

    // Endpoint para crear un nuevo movimiento
    @PostMapping("/crear")
    public ResponseEntity<Movimiento> crearMovimiento(@RequestBody MovimientoDTO movimientoDTO){
        try {
            Movimiento nuevoMovimiento = movimientoService.crearMovimiento(movimientoDTO);
            return new ResponseEntity<>(nuevoMovimiento, HttpStatus.CREATED);
        } catch (CuentaNoEncontradaException | SaldoInsuficienteException | IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para obtener un movimiento por su ID
    @GetMapping("/{id}")
    public  ResponseEntity<MovimientoListadoDTO> obtenerMovimientoPorId(@PathVariable("id") Long id){
        Optional<MovimientoListadoDTO> movimiento = movimientoService.obtenerMovimientoPorId(id);
        return movimiento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar un movimiento existente
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoObjectDTO> actualizarMovimiento(@PathVariable("id") Long id, @RequestBody MovimientoObjectDTO movimientoDTO){
        MovimientoObjectDTO movimientoActualizado = movimientoService.actualizarMovimiento(id, movimientoDTO);
        if (movimientoActualizado != null) {
            return new ResponseEntity<>(movimientoActualizado, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable("id") Long id){
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscarPorFechaYCliente")
    public ResponseEntity<List<MovimientoListadoDTO>> obtenerMovimientosPorFechaYCliente(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate fechaFin,
            @RequestParam Long clienteId){

        List<MovimientoListadoDTO> movimientos = movimientoService.obtenerMovimientosPorFechaYCliente(fechaInicio,fechaFin,clienteId);
        return  new ResponseEntity<>(movimientos, HttpStatus.OK);
    }


}
