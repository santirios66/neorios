package com.neorios.neorios.service;

import com.neorios.neorios.model.MetaAhorro;
import com.neorios.neorios.model.Usuario;
import com.neorios.neorios.repository.MetaAhorroRepository;
import com.neorios.neorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetaAhorroService {

    @Autowired
    private MetaAhorroRepository metaAhorroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public MetaAhorro crearMeta(Long usuarioId, String nombre, Double montoObjetivo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (montoObjetivo <= 0) {
            throw new RuntimeException("El monto objetivo debe ser mayor a cero");
        }

        MetaAhorro meta = new MetaAhorro();
        meta.setUsuario(usuario);
        meta.setNombre(nombre);
        meta.setMontoObjetivo(montoObjetivo);
        meta.setMontoActual(0.0);
        meta.setFechaCreacion(LocalDateTime.now());

        return metaAhorroRepository.save(meta);
    }

    public MetaAhorro abonar(Long metaId, Double monto) {
        MetaAhorro meta = metaAhorroRepository.findById(metaId)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        if (monto <= 0) {
            throw new RuntimeException("El monto a abonar debe ser mayor a cero");
        }

        meta.setMontoActual(meta.getMontoActual() + monto);
        return metaAhorroRepository.save(meta);
    }

    public List<MetaAhorro> listarMetasDeUsuario(Long usuarioId) {
        return metaAhorroRepository.findByUsuarioId(usuarioId);
    }
}