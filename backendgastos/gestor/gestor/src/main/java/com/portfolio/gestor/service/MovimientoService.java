package com.portfolio.gestor.service;

import com.portfolio.gestor.DTO.CuentaDTO;
import com.portfolio.gestor.DTO.MovimientoDTO;
import com.portfolio.gestor.DTO.MovimientoListadoDTO;
import com.portfolio.gestor.DTO.MovimientoObjectDTO;
import com.portfolio.gestor.entity.Cuenta;
import com.portfolio.gestor.entity.Movimiento;
import com.portfolio.gestor.entity.Persona;
import com.portfolio.gestor.exceptions.CuentaNoEncontradaException;
import com.portfolio.gestor.exceptions.SaldoInsuficienteException;
import com.portfolio.gestor.repository.MovimientoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    private static final Logger logger = LoggerFactory.getLogger(MovimientoService.class);

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaService cuentaService;

    public List<MovimientoListadoDTO> findTop5cuentaIdOrderByFechaDesc(Long cuentaId){
        List<Movimiento> movimientos = movimientoRepository.findTop5ByCuentaIdOrderByFechaDesc(cuentaId);
        return movimientos.stream().map(this::convertirAMovimientoDTO).collect(Collectors.toList());
    }

    public List<MovimientoListadoDTO> findByCuentaIdOrderByFechaDesc(Long cuentaId){
        List<Movimiento> movimientos = movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId);
        return movimientos.stream().map(this::convertirAMovimientoDTO).collect(Collectors.toList());
    }
    public Movimiento crearMovimiento(MovimientoDTO movimientoDTO) throws CuentaNoEncontradaException, SaldoInsuficienteException {
        Long cuentaOrigenId = movimientoDTO.getCuentaOrigenId();
        Long cuentaDestinoId = movimientoDTO.getCuentaDestinoId();
        Double valor = movimientoDTO.getValor();
        String tipoMovimiento = movimientoDTO.getTipoMovimiento();

        logger.info("Creando movimiento: TipoMovimiento={}, CuentaOrigenId={}, CuentaDestinoId={}, Valor={}",
                tipoMovimiento, cuentaOrigenId, cuentaDestinoId, valor);

        switch (tipoMovimiento)
        {
            case "Traspaso":
                if (cuentaOrigenId != null && cuentaDestinoId != null) {
                    return crearTraspaso(cuentaOrigenId, cuentaDestinoId, valor);
                }
                break;

            case "Retiro":
                if (cuentaOrigenId != null) {
                    return crearMovimientoEnCuenta(cuentaOrigenId, valor, "Retiro");
                }
                break;
            case "Deposito":
                if (cuentaDestinoId != null) {
                    return crearMovimientoEnCuenta(cuentaDestinoId, valor, "Deposito");
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de movimiento no reconocido");
        }
        throw new IllegalArgumentException("No se pudo determinar el tipo de movimiento.");
    }


    @Transactional
    private Movimiento crearTraspaso(Long cuentaOrigenId, Long cuentaDestinoId, Double valor)
            throws CuentaNoEncontradaException, SaldoInsuficienteException {
        logger.info("Creando traspaso: CuentaOrigenId={}, CuentaDestinoId={}, Valor={}", cuentaOrigenId, cuentaDestinoId, valor);
        CuentaDTO cuentaOrigen = cuentaService.obtenerCuentaPorId(cuentaOrigenId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta de origen no encontrada con ID: " + cuentaOrigenId));
        logger.info("Cuenta de origen obtenida: {}", cuentaOrigen);

        CuentaDTO cuentaDestino = cuentaService.obtenerCuentaPorId(cuentaDestinoId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta de destino no encontrada con ID: " + cuentaDestinoId));
        logger.info("Cuenta de destino obtenida: {}", cuentaDestino);

        // Verificar saldo suficiente en la cuenta de origen
        if (valor > 0 && ((CuentaDTO) cuentaOrigen).getSaldoInicial() < valor) {
            logger.warn("Saldo insuficiente en la cuenta de origen con ID: " + cuentaOrigenId);
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta de origen con ID: " + cuentaOrigenId);
        }

        // Actualizar saldos de las cuentas
        Double nuevoSaldoOrigen = cuentaOrigen.getSaldoInicial() - valor;
        cuentaOrigen.setSaldoInicial(nuevoSaldoOrigen);
        cuentaService.actualizarCuenta(cuentaOrigenId, cuentaOrigen);
        logger.info("Nuevo saldo en la cuenta de origen (ID: {}): {}", cuentaOrigenId, nuevoSaldoOrigen);

        if (cuentaOrigen.getClienteDTO() != null) {
            logger.info("ID del ClienteDTO en cuentaOrigen: {}", cuentaOrigen.getClienteDTO().getId());
        } else {
            logger.warn("ClienteDTO es nulo en cuentaOrigen.");
        }

        if (cuentaDestino.getClienteDTO() != null) {
            logger.info("ID del ClienteDTO en cuentaDestino: {}", cuentaDestino.getClienteDTO().getId());
        } else {
            logger.warn("ClienteDTO es nulo en cuentaDestino.");
        }

        Double nuevoSaldoDestino = cuentaDestino.getSaldoInicial() + valor;
        cuentaDestino.setSaldoInicial(nuevoSaldoDestino);
        cuentaService.actualizarCuenta(cuentaDestinoId, cuentaDestino);
        logger.info("Nuevo saldo en la cuenta de destino (ID: {}): {}", cuentaDestinoId, nuevoSaldoDestino);


        logger.info("ID obtenido de cuentaOrigenDTO: {}", cuentaOrigen.getId());
        logger.info("ID obtenido de cuentaDestinoDTO: {}", cuentaDestino.getId());


        Movimiento movimientoOrigen = new Movimiento();
        movimientoOrigen.setFecha(LocalDate.now());
        movimientoOrigen.setTipoMovimiento("Traspaso enviado");
        movimientoOrigen.setValor(-valor);
        movimientoOrigen.setSaldo(nuevoSaldoOrigen);
        movimientoOrigen.setCuenta(cuentaService.convertToEntity(cuentaOrigen));
        movimientoRepository.save(movimientoOrigen);
        logger.info("Movimiento registrado en la cuenta de origen: {}", movimientoOrigen);


        Movimiento movimientoDestino = new Movimiento();
        movimientoDestino.setFecha(LocalDate.now());
        movimientoDestino.setTipoMovimiento("Traspaso recibido");
        movimientoDestino.setValor(valor);
        movimientoDestino.setSaldo(nuevoSaldoDestino);
        movimientoDestino.setCuenta(cuentaService.convertToEntity(cuentaDestino));
        movimientoRepository.save(movimientoDestino);
        logger.info("Movimiento registrado en la cuenta de destino: {}", movimientoDestino);


        return movimientoDestino;
    }

    private Movimiento crearMovimientoEnCuenta(Long cuentaId, double valor, String tipoMovimiento)
            throws CuentaNoEncontradaException {
        logger.info("Creando movimiento en cuenta: CuentaId={}, Valor={}, TipoMovimiento={}", cuentaId, valor, tipoMovimiento);
        CuentaDTO cuenta = cuentaService.obtenerCuentaPorId(cuentaId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con ID: " + cuentaId));
        logger.info("Cuenta obtenida: {}", cuenta);

        Double nuevoSaldo;
        Double nuevoValor;
        if ("Retiro".equals(tipoMovimiento)) {
            if (cuenta.getSaldoInicial() < valor) {
                throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta con ID: " + cuentaId);
            }
            nuevoSaldo = cuenta.getSaldoInicial() - valor;
            nuevoValor = -valor;
        } else {
            nuevoSaldo = cuenta.getSaldoInicial() + valor;
            nuevoValor = valor;
        }

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaService.actualizarCuenta(cuentaId, cuenta);
        logger.info("Nuevo saldo en la cuenta (ID: {}): {}", cuentaId, nuevoSaldo);


        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDate.now());
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setValor(nuevoValor);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuentaService.convertToEntity(cuenta));

        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);
        logger.info("Movimiento registrado: {}", movimientoGuardado);


        return movimientoGuardado;
    }

    public List<MovimientoListadoDTO> obtenerTodosMovimientos() {
        List<Movimiento> movimientos = movimientoRepository.findAll();
        return movimientos.stream().map(this::convertirAMovimientoDTO).collect(Collectors.toList());
    }

    public Optional<MovimientoListadoDTO> obtenerMovimientoPorId(Long id) {
        Optional<Movimiento> movimiento = movimientoRepository.findById(id);
        return movimiento.map(this::convertirAMovimientoDTO);
    }

    public MovimientoObjectDTO actualizarMovimiento(Long id, MovimientoObjectDTO movimientoActualizado) {
        Optional<Movimiento> movimientoExistente = movimientoRepository.findById(id);
        if (movimientoExistente.isPresent()) {
            Movimiento movimiento = movimientoExistente.get();
            Optional<CuentaDTO> cuenta = cuentaService.obtenerCuentaPorId(movimiento.getCuenta().getId());

            if (cuenta.isPresent()){
                CuentaDTO cuentaActualizada = cuenta.get();

                // Revertir el efecto del movimiento anterior
                if ("Retiro".equals(movimiento.getTipoMovimiento()) || "Traspaso enviado".equals(movimiento.getTipoMovimiento())) {
                    cuentaActualizada.setSaldoInicial(cuentaActualizada.getSaldoInicial() + movimiento.getValor());
                } else {
                    cuentaActualizada.setSaldoInicial(cuentaActualizada.getSaldoInicial() - movimiento.getValor());
                }

                // Aplicar el nuevo movimiento
                Double nuevoSaldo;
                if ("Retiro".equals(movimientoActualizado.getTipoMovimiento()) || "Traspaso enviado".equals(movimientoActualizado.getTipoMovimiento())) {
                    nuevoSaldo = cuentaActualizada.getSaldoInicial() - movimientoActualizado.getValor();
                } else {
                    nuevoSaldo = cuentaActualizada.getSaldoInicial() + movimientoActualizado.getValor();
                }

                // Verificar si el saldo es suficiente para realizar la actualización
                if (nuevoSaldo < 0) {
                    throw new SaldoInsuficienteException("Saldo insuficiente para realizar la operación");
                }

                cuentaActualizada.setSaldoInicial(nuevoSaldo);
                cuentaService.actualizarCuenta(cuentaActualizada.getId(), cuentaActualizada);

                // Actualizar el movimiento
                movimiento.setSaldo(nuevoSaldo);
                movimiento.setTipoMovimiento(movimientoActualizado.getTipoMovimiento());
                movimiento.setValor(movimientoActualizado.getValor());
                movimiento.setFecha(movimientoActualizado.getFecha());
                Movimiento nuevoMovimiento = movimientoRepository.save(movimiento);

                return convertirAMovimientoObjectDTO(nuevoMovimiento);

            }
        }
        return null;
    }

    public void eliminarMovimiento(Long id) {
        movimientoRepository.deleteById(id);
    }

    public List<MovimientoListadoDTO> obtenerMovimientosPorFechaYCliente(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId) {
        List<Movimiento> movimientos = movimientoRepository.findByFechaBetweenAndCuentaClienteClienteId(fechaInicio,fechaFin,clienteId);
        return movimientos.stream().map(this::convertirAMovimientoDTO).collect(Collectors.toList());
    }
    private MovimientoListadoDTO convertirAMovimientoDTO(Movimiento movimiento) {
        Cuenta cuenta = movimiento.getCuenta();
        Persona persona = cuenta.getCliente().getPersona();

        MovimientoListadoDTO dto = new MovimientoListadoDTO();
        dto.setFecha(movimiento.getFecha());
        dto.setCliente(persona.getNombre());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipo(cuenta.getTipoCuenta());
        dto.setSaldoInicial(cuenta.getSaldoInicial());
        dto.setEstado(cuenta.getEstado());
        dto.setMovimiento(movimiento.getValor());
        dto.setSaldoDisponible(movimiento.getSaldo());
        return dto;
    }

    private MovimientoObjectDTO convertirAMovimientoObjectDTO(Movimiento movimiento){
        MovimientoObjectDTO dto = new MovimientoObjectDTO();
        dto.setFecha(movimiento.getFecha());
        dto.setSaldo(movimiento.getSaldo());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(movimiento.getValor());
        return dto;
    }
}

