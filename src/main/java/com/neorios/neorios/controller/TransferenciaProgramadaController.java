package com.neorios.neorios.controller;

import com.neorios.neorios.model.TransferenciaProgramada;
import com.neorios.neorios.service.TransferenciaProgramadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programadas")
public class TransferenciaProgramadaController {

    @Autowired
    private TransferenciaProgramadaService programadaService;

    @PostMapping("/crear")
    public TransferenciaProgramada crear(@RequestParam String cuentaOrigen,
                                          @RequestParam String cuentaDestino,
                                          @RequestParam Double monto,
                                          @RequestParam Integer diaDelMes) {
        return programadaService.crear(cuentaOrigen, cuentaDestino, monto, diaDelMes);
    }

    @PostMapping("/ejecutar/{programadaId}")
    public String ejecutar(@PathVariable Long programadaId) {
        programadaService.ejecutarProgramada(programadaId);
        return "Transferencia programada ejecutada correctamente";
    }

    @GetMapping("/cuenta/{cuentaOrigenId}")
    public List<TransferenciaProgramada> listar(@PathVariable Long cuentaOrigenId) {
        return programadaService.listarPorCuenta(cuentaOrigenId);
    }
}