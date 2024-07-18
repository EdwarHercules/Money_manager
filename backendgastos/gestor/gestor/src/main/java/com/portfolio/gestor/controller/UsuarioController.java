package com.portfolio.gestor.controller;


import com.portfolio.gestor.DTO.CuentaDTO;
import com.portfolio.gestor.entity.Persona;
import com.portfolio.gestor.entity.Usuario;
import com.portfolio.gestor.service.PersonaService;
import com.portfolio.gestor.service.UsuarioServicio;
import com.portfolio.gestor.service.UsuarioServicioImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioServicioImpl usuarioServicioimpl;

    @Autowired
    private PersonaService personaService;

    @GetMapping("/nombrePersona")
    public ResponseEntity<String> obtenerNombrePersona(@RequestParam String email) {
        try {
            String nombrePersona = usuarioServicioimpl.obtenerNombrePersonaPorEmail(email);
            logger.info(nombrePersona);
            return ResponseEntity.ok(nombrePersona);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el nombre de la persona");
        }
    }

    @GetMapping("/cuentas")
    public ResponseEntity<List<CuentaDTO>> obtenerCuentas(@RequestParam String email) {
        try {
            List<CuentaDTO> cuentas = usuarioServicioimpl.obtenerCuentasPorEmailUsuario(email);
            return ResponseEntity.ok(cuentas);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
