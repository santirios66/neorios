package com.neorios.neorios.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transferencias_programadas")
public class TransferenciaProgramada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cuenta_origen_id", nullable = false)
    private Cuenta cuentaOrigen;

    @ManyToOne
    @JoinColumn(name = "cuenta_destino_id", nullable = false)
    private Cuenta cuentaDestino;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private Integer diaDelMes; // día en que se debe ejecutar (1-28)

    @Column(nullable = false)
    private Boolean activa = true;

    @Column
    private LocalDate ultimaEjecucion;

    public TransferenciaProgramada() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cuenta getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(Cuenta cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }

    public Cuenta getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(Cuenta cuentaDestino) { this.cuentaDestino = cuentaDestino; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public Integer getDiaDelMes() { return diaDelMes; }
    public void setDiaDelMes(Integer diaDelMes) { this.diaDelMes = diaDelMes; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }

    public LocalDate getUltimaEjecucion() { return ultimaEjecucion; }
    public void setUltimaEjecucion(LocalDate ultimaEjecucion) { this.ultimaEjecucion = ultimaEjecucion; }
}