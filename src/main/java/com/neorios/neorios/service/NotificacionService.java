package com.neorios.neorios.service;

import com.neorios.neorios.model.Notificacion;
import com.neorios.neorios.model.Usuario;
import com.neorios.neorios.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public void crearNotificacion(Usuario usuario, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setMensaje(mensaje);
        notificacion.setLeida(false);
        notificacion.setFecha(LocalDateTime.now());

        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listarNotificaciones(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public Notificacion marcarComoLeida(Long notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }
}