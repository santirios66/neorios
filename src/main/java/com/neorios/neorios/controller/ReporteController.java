package com.neorios.neorios.controller;

import com.neorios.neorios.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/general")
    public Map<String, Object> reporteGeneral() {
        return reporteService.generarReporteGeneral();
    }
}