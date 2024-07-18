package com.portfolio.gestor.service;

import com.portfolio.gestor.DTO.ClienteDTO;
import com.portfolio.gestor.entity.Cliente;
import com.portfolio.gestor.entity.Persona;
import com.portfolio.gestor.repository.ClienteRepository;
import com.portfolio.gestor.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    // metodo para crear un cliente
    @Transactional
    public Cliente crearCliente(ClienteDTO clienteDTO){
        Persona persona = new Persona(clienteDTO.getNombre(), clienteDTO.getGenero(),
                clienteDTO.getEdad(), clienteDTO.getIdentificacion(), clienteDTO.getDireccion(),
                clienteDTO.getTelefono());
        persona = personaRepository.save(persona);

        Cliente cliente = new Cliente();
        cliente.setPassword(clienteDTO.getPassword());
        cliente.setEstado(true);
        cliente.setPersona(persona);

        return clienteRepository.save(cliente);
    }

    // Metodo para obtener todos los clientes
    public List<Cliente> obtenerTodosClientes(){
        return clienteRepository.findAll();
    }

    // Metodo para obtener un cliente por su ID
    public Optional<Cliente> obtenerClientePorId(Long id){
        return clienteRepository.findById(id);
    }

    // Metodo para actualizar un cliente
    @Transactional
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        Optional<Cliente> clienteExistenteOptional = clienteRepository.findById(id);
        if (clienteExistenteOptional.isPresent()) {
            Cliente clienteExistente = clienteExistenteOptional.get();

            // Actualizar los datos del cliente existente con los datos del DTO
            Persona persona = clienteExistente.getPersona();
            persona.setNombre(clienteDTO.getNombre());
            persona.setGenero(clienteDTO.getGenero());
            persona.setEdad(clienteDTO.getEdad());
            persona.setIdentificacion(clienteDTO.getIdentificacion());
            persona.setDireccion(clienteDTO.getDireccion());
            persona.setTelefono(clienteDTO.getTelefono());

            clienteExistente.setPassword(clienteDTO.getPassword());

            // Guardar y retornar el cliente actualizado
            clienteExistente = clienteRepository.save(clienteExistente);
            return convertirAClienteDTO(clienteExistente);
        }
        return null; // Manejar el caso donde el cliente no existe
    }


    // Metodo para eliminar un cliente por su ID
    public void eliminarCliente(Long id){
        clienteRepository.deleteById(id);
    }


    private ClienteDTO convertirAClienteDTO(Cliente cliente) {
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
