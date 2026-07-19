package com.neorios.neorios.controller;

import com.neorios.neorios.model.Cdt;
import com.neorios.neorios.service.CdtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cdts")
public class CdtController {

    @Autowired
    private CdtService cdtService;

    @GetMapping("/simular")
    public Double simular(@RequestParam Double monto,
                           @RequestParam Double tasaInteresAnual,
                           @RequestParam Integer plazoMeses) {
        return cdtService.simularMontoFinal(monto, tasaInteresAnual, plazoMeses);
    }

    @PostMapping("/abrir/{usuarioId}")
    public Cdt abrir(@PathVariable Long usuarioId,
                      @RequestParam Double monto,
                      @RequestParam Double tasaInteresAnual,
                      @RequestParam Integer plazoMeses) {
        return cdtService.abrirCdt(usuarioId, monto, tasaInteresAnual, plazoMeses);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Cdt> listar(@PathVariable Long usuarioId) {
        return cdtService.listarCdtsDeUsuario(usuarioId);
    }
}