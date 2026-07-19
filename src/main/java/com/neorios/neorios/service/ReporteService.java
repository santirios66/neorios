package com.neorios.neorios.service;

import com.neorios.neorios.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReporteService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private CreditoRepository creditoRepository;

    public Map<String, Object> generarReporteGeneral() {
        Map<String, Object> reporte = new HashMap<>();

        long totalUsuarios = usuarioRepository.count();
        long totalCuentas = cuentaRepository.count();
        long totalTransacciones = transaccionRepository.count();

        double saldoTotalEnElBanco = cuentaRepository.findAll()
                .stream()
                .mapToDouble(c -> c.getSaldo())
                .sum();

        long creditosPendientes = creditoRepository.findByEstado("PENDIENTE").size();
        long creditosAprobados = creditoRepository.findByEstado("APROBADO").size();
        long creditosRechazados = creditoRepository.findByEstado("RECHAZADO").size();

        reporte.put("totalUsuarios", totalUsuarios);
        reporte.put("totalCuentas", totalCuentas);
        reporte.put("totalTransacciones", totalTransacciones);
        reporte.put("saldoTotalEnElBanco", saldoTotalEnElBanco);
        reporte.put("creditosPendientes", creditosPendientes);
        reporte.put("creditosAprobados", creditosAprobados);
        reporte.put("creditosRechazados", creditosRechazados);

        return reporte;
    }
}