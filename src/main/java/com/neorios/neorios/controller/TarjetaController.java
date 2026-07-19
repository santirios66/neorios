package com.neorios.neorios.controller;

import com.neorios.neorios.model.Tarjeta;
import com.neorios.neorios.service.TarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    @PostMapping("/solicitar/{cuentaId}")
    public Tarjeta solicitar(@PathVariable Long cuentaId, @RequestParam String tipoTarjeta) {
        return tarjetaService.solicitarTarjeta(cuentaId, tipoTarjeta);
    }

    @PutMapping("/{tarjetaId}/estado")
    public Tarjeta cambiarEstado(@PathVariable Long tarjetaId, @RequestParam String nuevoEstado) {
        return tarjetaService.cambiarEstado(tarjetaId, nuevoEstado);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public List<Tarjeta> listar(@PathVariable Long cuentaId) {
        return tarjetaService.listarTarjetasDeCuenta(cuentaId);
    }
}