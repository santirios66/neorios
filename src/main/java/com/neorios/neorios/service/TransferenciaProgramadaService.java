package com.neorios.neorios.service;

import com.neorios.neorios.model.Cuenta;
import com.neorios.neorios.model.TransferenciaProgramada;
import com.neorios.neorios.repository.CuentaRepository;
import com.neorios.neorios.repository.TransferenciaProgramadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransferenciaProgramadaService {

    @Autowired
    private TransferenciaProgramadaRepository programadaRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private TransaccionService transaccionService;

    public TransferenciaProgramada crear(String numeroCuentaOrigen, String numeroCuentaDestino,
                                          Double monto, Integer diaDelMes) {
        Cuenta origen = cuentaRepository.findByNumeroCuenta(numeroCuentaOrigen)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada"));
        Cuenta destino = cuentaRepository.findByNumeroCuenta(numeroCuentaDestino)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada"));

        if (diaDelMes < 1 || diaDelMes > 28) {
            throw new RuntimeException("El día del mes debe estar entre 1 y 28");
        }

        TransferenciaProgramada programada = new TransferenciaProgramada();
        programada.setCuentaOrigen(origen);
        programada.setCuentaDestino(destino);
        programada.setMonto(monto);
        programada.setDiaDelMes(diaDelMes);
        programada.setActiva(true);

        return programadaRepository.save(programada);
    }

    public void ejecutarProgramada(Long programadaId) {
        TransferenciaProgramada programada = programadaRepository.findById(programadaId)
                .orElseThrow(() -> new RuntimeException("Transferencia programada no encontrada"));

        if (!programada.getActiva()) {
            throw new RuntimeException("Esta transferencia programada está inactiva");
        }

        transaccionService.transferir(
                programada.getCuentaOrigen().getNumeroCuenta(),
                programada.getCuentaDestino().getNumeroCuenta(),
                programada.getMonto()
        );

        programada.setUltimaEjecucion(LocalDate.now());
        programadaRepository.save(programada);
    }


    @Scheduled(cron = "0 0 6 * * *")
    public void ejecutarTransferenciasDelDia() {
        LocalDate hoy = LocalDate.now();
        int diaHoy = hoy.getDayOfMonth();

        List<TransferenciaProgramada> activas = programadaRepository.findByActivaTrue();

        for (TransferenciaProgramada programada : activas) {
            if (programada.getDiaDelMes().equals(diaHoy)) {
                try {
                    ejecutarProgramada(programada.getId());
                } catch (Exception e) {
                    System.out.println("Error ejecutando programada #" + programada.getId() + ": " + e.getMessage());
                }
            }
        }
    }

    public List<TransferenciaProgramada> listarPorCuenta(Long cuentaOrigenId) {
        return programadaRepository.findByCuentaOrigenId(cuentaOrigenId);
    }
}