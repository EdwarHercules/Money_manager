package com.portfolio.gestor.controller;


import com.portfolio.gestor.DTO.PersonaDTO;
import com.portfolio.gestor.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;


    // Endpoint para obtener todas las personas
    @GetMapping
    public ResponseEntity<List<PersonaDTO>> obtenerTodasPersonas(){
        List<PersonaDTO> personas = personaService.obtenerTodasPersonas();
        return  new ResponseEntity<>(personas, HttpStatus.OK);
    }


    // Endpoint para obtener una personas por su ID
    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> obtenerPersonaPorId(@PathVariable("id") Long id){
        Optional<PersonaDTO> persona = personaService.obtenerPersonaPorId(id);
        return persona.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar una persona existente
    @PutMapping("/{id}")
    public ResponseEntity<PersonaDTO> actualizarPersona(@PathVariable("id") Long id, @RequestBody PersonaDTO personaActualizada){
        PersonaDTO personaActualizadaresult = personaService.actualizarPersona(id, personaActualizada);
        return personaActualizadaresult != null ?
                ResponseEntity.ok(personaActualizadaresult) :
                ResponseEntity.notFound().build();
    }

    // Endpoint para eliminar una persona por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable("id") Long id){
        personaService.eliminarPersona(id);
        return ResponseEntity.noContent().build();
    }
}
