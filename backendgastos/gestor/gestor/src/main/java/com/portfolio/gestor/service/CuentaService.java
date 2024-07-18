package com.portfolio.gestor.service;


import com.portfolio.gestor.DTO.ClienteDTO;
import com.portfolio.gestor.DTO.CuentaDTO;
import com.portfolio.gestor.entity.Cliente;
import com.portfolio.gestor.entity.Cuenta;
import com.portfolio.gestor.exceptions.CuentaNoEncontradaException;
import com.portfolio.gestor.repository.ClienteRepository;
import com.portfolio.gestor.repository.CuentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    private static final Logger logger = LoggerFactory.getLogger(CuentaService.class);

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Metodo para crear una cuenta para un cliente existente
    public CuentaDTO crearCuentaParaCliente(Long clienteId, CuentaDTO cuentaDTO){
        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteId);
        if(clienteOptional.isPresent()){
            Cliente cliente = clienteOptional.get();

            Cuenta cuenta = new Cuenta();
            cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
            cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
            cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());
            cuenta.setEstado(true);
            cuenta.setCliente(cliente);

            Cuenta savedCuenta = cuentaRepository.save(cuenta);

            return convertToDTO(savedCuenta);
        } else {
            throw new CuentaNoEncontradaException("Cliente no encontrado con ID: " + clienteId);
        }
    }

    public void actualizarCuentasPorCliente(Cliente cliente){
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(cliente.getId());

        for (Cuenta cuenta : cuentas){
            cuenta.setEstado(cliente.getEstado());
            cuentaRepository.save(cuenta);
        }
    }

    public List<CuentaDTO> findByClienteId(Long clienteId){
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        return cuentas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Metodo para obtener todas las cuentas
    public List<CuentaDTO> obtenerTodasCuentas(){
        List<Cuenta> cuentas = cuentaRepository.findAll();
        return cuentas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Método para obtener una cuenta por su ID
    public Optional<CuentaDTO> obtenerCuentaPorId(Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        return cuenta.map(this :: convertToDTO);
    }

    // Metodo para actualizar una cuenta
    public CuentaDTO actualizarCuenta(Long id, CuentaDTO cuentaDTOActualizada){
        if (cuentaRepository.existsById(id)){
            Cuenta cuenta = cuentaRepository.findById(id).orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con ID: " + id));

            cuenta.setNumeroCuenta(cuentaDTOActualizada.getNumeroCuenta());
            cuenta.setTipoCuenta(cuentaDTOActualizada.getTipoCuenta());
            cuenta.setSaldoInicial(cuentaDTOActualizada.getSaldoInicial());

            Cuenta updateCuenta = cuentaRepository.save(cuenta);
            return convertToDTO(updateCuenta);
        } else {
            throw new CuentaNoEncontradaException("Cuenta no encontrada con ID: " + id);
        }
    }

    // Metodo para eliminar una cuenta
    public void eliminarCuenta(Long id){
        cuentaRepository.deleteById(id);
    }


    private CuentaDTO convertToDTO(Cuenta cuenta) {
        return new CuentaDTO(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getEstado(),
                new ClienteDTO(cuenta.getCliente().getId(), cuenta.getCliente().getPersona().getNombre()) // Assuming ClienteDTO has these fields
        );
    }

    public Cuenta convertToEntity(CuentaDTO cuentaDTO){
        try {

            Cuenta cuenta = new Cuenta();
            cuenta.setId(cuentaDTO.getId());
            cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
            cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());
            cuenta.setEstado(cuentaDTO.isEstado());

            logger.info("ID obtenido de cuentaDTO: {}", cuenta.getId());
            return cuenta;
        } catch (Exception e){
            logger.error("Error al convertir CuentaDTO a entidad Cuenta: {}", e.getMessage());
            throw e;
        }

    }
    private Cliente converToEntity(ClienteDTO clienteDTO){
        Cliente cliente = new Cliente();
        cliente.setId(clienteDTO.getId());
        cliente.setPassword(clienteDTO.getPassword());
        return cliente;
    }


}
