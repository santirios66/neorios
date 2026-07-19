package com.neorios.neorios.controller;

import com.neorios.neorios.model.Credito;
import com.neorios.neorios.service.CreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    @GetMapping("/simular")
    public Double simular(@RequestParam Double monto,
                           @RequestParam Double tasaInteresAnual,
                           @RequestParam Integer plazoMeses) {
        return creditoService.simularCuota(monto, tasaInteresAnual, plazoMeses);
    }

    @PostMapping("/solicitar/{usuarioId}")
    public Credito solicitar(@PathVariable Long usuarioId,
                              @RequestParam Double monto,
                              @RequestParam Double tasaInteresAnual,
                              @RequestParam Integer plazoMeses) {
        return creditoService.solicitarCredito(usuarioId, monto, tasaInteresAnual, plazoMeses);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Credito> listar(@PathVariable Long usuarioId) {
        return creditoService.listarCreditosDeUsuario(usuarioId);
    }

    @GetMapping("/pendientes")
    public List<Credito> pendientes() {
        return creditoService.listarTodosPendientes();
    }

    @PutMapping("/{creditoId}/estado")
    public Credito cambiarEstado(@PathVariable Long creditoId, @RequestParam String nuevoEstado) {
        return creditoService.aprobarORechazar(creditoId, nuevoEstado);
    }
}