package com.portfolio.gestor.repository;

import com.portfolio.gestor.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByFechaBetweenAndCuentaClienteClienteId(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId);

    List<Movimiento> findByFechaBetweenAndCuentaId(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId);


    List<Movimiento> findTop5ByCuentaIdOrderByFechaDesc(Long cuentaId);
    List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);
}
