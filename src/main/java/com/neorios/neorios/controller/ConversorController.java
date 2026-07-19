package com.neorios.neorios.controller;

import com.neorios.neorios.service.ConversorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversor")
public class ConversorController {

    @Autowired
    private ConversorService conversorService;

    @GetMapping("/convertir")
    public Double convertir(@RequestParam String monedaOrigen,
                             @RequestParam String monedaDestino,
                             @RequestParam Double monto) {
        return conversorService.convertir(monedaOrigen, monedaDestino, monto);
    }
}