package com.portfolio.gestor.repository;

import com.portfolio.gestor.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByPersonaId(Long personaId);

}
