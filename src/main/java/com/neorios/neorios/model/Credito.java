package com.neorios.neorios.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "creditos")
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private Double tasaInteresAnual; // ejemplo: 18.5 (representa 18.5%)

    @Column(nullable = false)
    private Integer plazoMeses;

    @Column(nullable = false)
    private Double cuotaMensual;

    @Column(nullable = false)
    private String estado; // "PENDIENTE", "APROBADO", "RECHAZADO"

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    public Credito() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public Double getTasaInteresAnual() { return tasaInteresAnual; }
    public void setTasaInteresAnual(Double tasaInteresAnual) { this.tasaInteresAnual = tasaInteresAnual; }

    public Integer getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(Integer plazoMeses) { this.plazoMeses = plazoMeses; }

    public Double getCuotaMensual() { return cuotaMensual; }
    public void setCuotaMensual(Double cuotaMensual) { this.cuotaMensual = cuotaMensual; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
} 