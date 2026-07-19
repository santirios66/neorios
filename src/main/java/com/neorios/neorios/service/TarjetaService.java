package com.neorios.neorios.service;

import com.neorios.neorios.model.Cuenta;
import com.neorios.neorios.model.Tarjeta;
import com.neorios.neorios.repository.CuentaRepository;
import com.neorios.neorios.repository.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public Tarjeta solicitarTarjeta(Long cuentaId, String tipoTarjeta) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setCuenta(cuenta);
        tarjeta.setTipoTarjeta(tipoTarjeta);
        tarjeta.setNumeroTarjeta(generarNumeroTarjeta());
        tarjeta.setCvv(generarCvv());
        tarjeta.setFechaVencimiento(LocalDate.now().plusYears(4));
        tarjeta.setEstado("ACTIVA");

        return tarjetaRepository.save(tarjeta);
    }

    public Tarjeta cambiarEstado(Long tarjetaId, String nuevoEstado) {
        Tarjeta tarjeta = tarjetaRepository.findById(tarjetaId)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        if (!nuevoEstado.equals("ACTIVA") && !nuevoEstado.equals("BLOQUEADA")) {
            throw new RuntimeException("Estado inválido, debe ser ACTIVA o BLOQUEADA");
        }

        tarjeta.setEstado(nuevoEstado);
        return tarjetaRepository.save(tarjeta);
    }

    public List<Tarjeta> listarTarjetasDeCuenta(Long cuentaId) {
        return tarjetaRepository.findByCuentaId(cuentaId);
    }

    private String generarNumeroTarjeta() {
        Random random = new Random();
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            numero.append(random.nextInt(10));
        }
        return numero.toString();
    }

    private String generarCvv() {
        Random random = new Random();
        int cvv = 100 + random.nextInt(900); // entre 100 y 999
        return String.valueOf(cvv);
    }
}