package com.neorios.neorios.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private String rol = "CLIENTE"; // "CLIENTE" o "ADMIN"
    @Column(nullable = false)
    private Boolean bloqueado = false;

    @Column
    private String codigoRecuperacion;

    @Column
    private LocalDateTime codigoExpiracion;

    // Constructor vacío (obligatorio para JPA)
    public Usuario() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Boolean getBloqueado() { return bloqueado; }
    public void setBloqueado(Boolean bloqueado) { this.bloqueado = bloqueado; }

    public String getCodigoRecuperacion() { return codigoRecuperacion; }
    public void setCodigoRecuperacion(String codigoRecuperacion) { this.codigoRecuperacion = codigoRecuperacion; }

    public LocalDateTime getCodigoExpiracion() { return codigoExpiracion; }
    public void setCodigoExpiracion(LocalDateTime codigoExpiracion) { this.codigoExpiracion = codigoExpiracion; }
}