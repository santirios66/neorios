package com.neorios.neorios.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cdts")
public class Cdt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Double montoInicial;

    @Column(nullable = false)
    private Double tasaInteresAnual;

    @Column(nullable = false)
    private Integer plazoMeses;

    @Column(nullable = false)
    private Double montoFinal;

    @Column(nullable = false)
    private LocalDateTime fechaApertura;

    public Cdt() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Double getMontoInicial() { return montoInicial; }
    public void setMontoInicial(Double montoInicial) { this.montoInicial = montoInicial; }

    public Double getTasaInteresAnual() { return tasaInteresAnual; }
    public void setTasaInteresAnual(Double tasaInteresAnual) { this.tasaInteresAnual = tasaInteresAnual; }

    public Integer getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(Integer plazoMeses) { this.plazoMeses = plazoMeses; }

    public Double getMontoFinal() { return montoFinal; }
    public void setMontoFinal(Double montoFinal) { this.montoFinal = montoFinal; }

    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }
}