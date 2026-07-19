package com.neorios.neorios.controller;

import com.neorios.neorios.model.LogAuditoria;
import com.neorios.neorios.service.LogAuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class LogAuditoriaController {

    @Autowired
    private LogAuditoriaService logAuditoriaService;

    @GetMapping("/todos")
    public List<LogAuditoria> listarTodos() {
        return logAuditoriaService.listarTodos();
    }
}