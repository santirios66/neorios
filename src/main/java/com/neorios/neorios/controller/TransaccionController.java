package com.neorios.neorios.controller;

import com.neorios.neorios.model.Transaccion;
import com.neorios.neorios.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.IOException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @PostMapping("/transferir")
    public Transaccion transferir(@RequestParam String cuentaOrigen,
                                    @RequestParam String cuentaDestino,
                                    @RequestParam Double monto) {
        return transaccionService.transferir(cuentaOrigen, cuentaDestino, monto);
    }

    @GetMapping("/historial/{numeroCuenta}")
    public List<Transaccion> historial(@PathVariable String numeroCuenta) {
        return transaccionService.obtenerHistorial(numeroCuenta);
    }

    @PutMapping("/revertir/{transaccionId}")
    public Transaccion revertir(@PathVariable Long transaccionId) {
        return transaccionService.revertirTransaccion(transaccionId);
    }

    @GetMapping("/comprobante/{transaccionId}")
    public String comprobante(@PathVariable Long transaccionId) {
        return transaccionService.generarComprobante(transaccionId);
    }

    @GetMapping("/extracto/{numeroCuenta}")
    public ResponseEntity<byte[]> extractoPdf(@PathVariable String numeroCuenta) throws IOException {
        byte[] pdf = transaccionService.generarExtractoPdf(numeroCuenta);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "extracto_" + numeroCuenta + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}