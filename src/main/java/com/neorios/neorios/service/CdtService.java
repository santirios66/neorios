package com.neorios.neorios.service;

import com.neorios.neorios.model.Cdt;
import com.neorios.neorios.model.Usuario;
import com.neorios.neorios.repository.CdtRepository;
import com.neorios.neorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CdtService {

    @Autowired
    private CdtRepository cdtRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Double simularMontoFinal(Double monto, Double tasaInteresAnual, Integer plazoMeses) {
        double tasaMensual = (tasaInteresAnual / 100) / 12;
        double montoFinal = monto * Math.pow(1 + tasaMensual, plazoMeses);
        return Math.round(montoFinal * 100.0) / 100.0;
    }

    public Cdt abrirCdt(Long usuarioId, Double monto, Double tasaInteresAnual, Integer plazoMeses) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (monto <= 0 || plazoMeses <= 0) {
            throw new RuntimeException("Monto y plazo deben ser mayores a cero");
        }

        Double montoFinal = simularMontoFinal(monto, tasaInteresAnual, plazoMeses);

        Cdt cdt = new Cdt();
        cdt.setUsuario(usuario);
        cdt.setMontoInicial(monto);
        cdt.setTasaInteresAnual(tasaInteresAnual);
        cdt.setPlazoMeses(plazoMeses);
        cdt.setMontoFinal(montoFinal);
        cdt.setFechaApertura(LocalDateTime.now());

        return cdtRepository.save(cdt);
    }

    public List<Cdt> listarCdtsDeUsuario(Long usuarioId) {
        return cdtRepository.findByUsuarioId(usuarioId);
    }
}