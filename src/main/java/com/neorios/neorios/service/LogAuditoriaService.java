package com.neorios.neorios.service;

import com.neorios.neorios.model.LogAuditoria;
import com.neorios.neorios.repository.LogAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogAuditoriaService {

    @Autowired
    private LogAuditoriaRepository logAuditoriaRepository;

    public void registrar(String accion, String detalle, Long usuarioId) {
        LogAuditoria log = new LogAuditoria();
        log.setAccion(accion);
        log.setDetalle(detalle);
        log.setUsuarioId(usuarioId);
        log.setFecha(LocalDateTime.now());

        logAuditoriaRepository.save(log);
    }

    public List<LogAuditoria> listarTodos() {
        return logAuditoriaRepository.findAllByOrderByFechaDesc();
    }
}