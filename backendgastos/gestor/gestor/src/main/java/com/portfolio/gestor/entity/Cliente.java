package com.portfolio.gestor.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long clienteId;

    @Column(name = "password")
    private String password;

    @Column(name = "estado")
    private Boolean estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id")
    private Persona persona;

    @OneToMany(mappedBy = "cliente")
    private List<Cuenta> cuentas;
    public Cliente() {
    }

    public Cliente(String password, Boolean estado, Persona persona) {
        this.password = password;
        this.estado = estado;
        this.persona = persona;
    }

    public Cliente(Long id, String password, Boolean estado, Persona persona) {
        this.clienteId = clienteId;
        this.password = password;
        this.estado = estado;
        this.persona = persona;
    }

    public Long getId() {
        return clienteId;
    }

    public void setId(Long id) {
        this.clienteId = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}

