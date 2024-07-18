package com.portfolio.gestor.controller;

import com.portfolio.gestor.DTO.ClienteDTO;
import com.portfolio.gestor.entity.Cliente;
import com.portfolio.gestor.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Endpoint para obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> obtenerTodosClientes(){
        List<Cliente> clientes = clienteService.obtenerTodosClientes();
        List<ClienteDTO> clienteDTOS = clientes.stream()
                .map(this::convertirAClienteDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clienteDTOS, HttpStatus.OK);
    }

    // Endpoint para obtener un cliente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable("id") Long id){
        Optional<Cliente> clienteOptional = clienteService.obtenerClientePorId(id);
        return clienteOptional.map(cliente -> ResponseEntity.ok(convertirAClienteDTO(cliente))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para crear un nuevo cliente
    @PostMapping("/crearUsuario")
    public ResponseEntity<Cliente> crearCliente(@RequestBody ClienteDTO clienteDTO){
        Cliente nuevoCliente = clienteService.crearCliente(clienteDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    // Endpoint para actualizar un cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable("id") Long id, @RequestBody ClienteDTO clienteDTO){
        ClienteDTO clienteActualizadoDTO = clienteService.actualizarCliente(id, clienteDTO);
        if (clienteActualizadoDTO != null){
            return ResponseEntity.ok(clienteActualizadoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para eliminar un cliente por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable("id") Long id){
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }


    private ClienteDTO convertirAClienteDTO(Cliente cliente){
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNombre(cliente.getPersona().getNombre());
        clienteDTO.setGenero(cliente.getPersona().getGenero());
        clienteDTO.setEdad(cliente.getPersona().getEdad());
        clienteDTO.setIdentificacion(cliente.getPersona().getIdentificacion());
        clienteDTO.setDireccion(cliente.getPersona().getDireccion());
        clienteDTO.setTelefono(cliente.getPersona().getTelefono());
        clienteDTO.setPassword(cliente.getPassword());
        return clienteDTO;
    }
}
