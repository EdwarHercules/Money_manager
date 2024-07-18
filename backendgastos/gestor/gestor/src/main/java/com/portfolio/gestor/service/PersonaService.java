package com.portfolio.gestor.service;

import com.portfolio.gestor.DTO.PersonaDTO;
import com.portfolio.gestor.entity.Persona;
import com.portfolio.gestor.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonaService {
    @Autowired
    private PersonaRepository personaRepository;

    public Persona crearPersona(Persona persona){
        return personaRepository.save(persona);
    }

    public List<PersonaDTO> obtenerTodasPersonas(){
        List<Persona> personas = personaRepository.findAll();
        return personas.stream().map(this::convertirAPersonaDTO).collect(Collectors.toList());
    }

    public Optional<PersonaDTO> obtenerPersonaPorId(Long id){
        Optional<Persona> persona = personaRepository.findById(id);
        return persona.map(this::convertirAPersonaDTO);
    }

    public PersonaDTO actualizarPersona(Long id, PersonaDTO personaActualizada) {
        Optional<Persona> personaOptional = personaRepository.findById(id);
        if (personaOptional.isPresent()) {
            Persona persona = personaOptional.get();
            persona.setNombre(personaActualizada.getNombre());
            persona.setTelefono(personaActualizada.getTelefono());
            persona.setEdad(personaActualizada.getEdad());
            persona.setIdentificacion(personaActualizada.getIdentificacion());
            persona.setDireccion(personaActualizada.getDireccion());
            persona.setGenero(personaActualizada.getGenero());
            Persona nuevaPersona = personaRepository.save(persona);
            return convertirAPersonaDTO(nuevaPersona);
        }
        return null;
    }

    public void eliminarPersona(Long id){
        personaRepository.deleteById(id);
    }

    private PersonaDTO convertirAPersonaDTO(Persona persona){
        PersonaDTO personaDTO =  new PersonaDTO();
        personaDTO.setId(persona.getId());
        personaDTO.setNombre(persona.getNombre());
        personaDTO.setDireccion(persona.getDireccion());
        personaDTO.setEdad(persona.getEdad());
        personaDTO.setGenero(persona.getGenero());
        personaDTO.setIdentificacion(persona.getIdentificacion());
        personaDTO.setTelefono(persona.getTelefono());
        return personaDTO;
    }
}
