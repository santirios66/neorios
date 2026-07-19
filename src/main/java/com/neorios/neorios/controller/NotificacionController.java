package com.neorios.neorios.controller;

import com.neorios.neorios.model.Notificacion;
import com.neorios.neorios.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/usuario/{usuarioId}")
    public List<Notificacion> listar(@PathVariable Long usuarioId) {
        return notificacionService.listarNotificaciones(usuarioId);
    }

    @PutMapping("/{notificacionId}/leida")
    public Notificacion marcarLeida(@PathVariable Long notificacionId) {
        return notificacionService.marcarComoLeida(notificacionId);
    }
}