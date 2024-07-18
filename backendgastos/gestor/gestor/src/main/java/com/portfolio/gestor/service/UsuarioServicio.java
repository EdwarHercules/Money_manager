package com.portfolio.gestor.service;

import com.portfolio.gestor.entity.Usuario;
import com.portfolio.gestor.DTO.UsuarioRegistroDTO;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UsuarioServicio extends UserDetailsService {
    public Usuario guardar(UsuarioRegistroDTO usuarioRegistroDTO);
}
