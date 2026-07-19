package com.neorios.neorios.controller;

import com.neorios.neorios.model.Cuenta;
import com.neorios.neorios.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping("/abrir/{usuarioId}")
    public Cuenta abrirCuenta(@PathVariable Long usuarioId, @RequestParam String tipoCuenta) {
        return cuentaService.abrirCuenta(usuarioId, tipoCuenta);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Cuenta> listarCuentas(@PathVariable Long usuarioId) {
        return cuentaService.listarCuentasDeUsuario(usuarioId);
    }

    @DeleteMapping("/{cuentaId}")
    public String cerrar(@PathVariable Long cuentaId) {
        cuentaService.cerrarCuenta(cuentaId);
        return "Cuenta cerrada exitosamente";
    }
}