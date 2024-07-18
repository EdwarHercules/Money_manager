package com.portfolio.gestor.repository;

import com.portfolio.gestor.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Rolrepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String role_user);
}
