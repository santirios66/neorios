package com.neorios.neorios.service;

import com.neorios.neorios.model.Credito;
import com.neorios.neorios.model.Usuario;
import com.neorios.neorios.repository.CreditoRepository;
import com.neorios.neorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditoService {

    @Autowired
    private CreditoRepository creditoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LogAuditoriaService logAuditoriaService;

    public Double simularCuota(Double monto, Double tasaInteresAnual, Integer plazoMeses) {
        double tasaMensual = (tasaInteresAnual / 100) / 12;

        double factor = Math.pow(1 + tasaMensual, plazoMeses);
        double cuota = monto * (tasaMensual * factor) / (factor - 1);

        return Math.round(cuota * 100.0) / 100.0; // redondeado a 2 decimales
    }

    public Credito solicitarCredito(Long usuarioId, Double monto, Double tasaInteresAnual, Integer plazoMeses) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (monto <= 0 || plazoMeses <= 0) {
            throw new RuntimeException("Monto y plazo deben ser mayores a cero");
        }

        Double cuota = simularCuota(monto, tasaInteresAnual, plazoMeses);

        Credito credito = new Credito();
        credito.setUsuario(usuario);
        credito.setMonto(monto);
        credito.setTasaInteresAnual(tasaInteresAnual);
        credito.setPlazoMeses(plazoMeses);
        credito.setCuotaMensual(cuota);
        credito.setEstado("PENDIENTE");
        credito.setFechaSolicitud(LocalDateTime.now());

        return creditoRepository.save(credito);
    }

    public List<Credito> listarCreditosDeUsuario(Long usuarioId) {
        return creditoRepository.findByUsuarioId(usuarioId);
    }

    public List<Credito> listarTodosPendientes() {
        return creditoRepository.findByEstado("PENDIENTE");
    }

    public Credito aprobarORechazar(Long creditoId, String nuevoEstado) {
        Credito credito = creditoRepository.findById(creditoId)
                .orElseThrow(() -> new RuntimeException("Crédito no encontrado"));

        if (!nuevoEstado.equals("APROBADO") && !nuevoEstado.equals("RECHAZADO")) {
            throw new RuntimeException("Estado inválido, debe ser APROBADO o RECHAZADO");
        }

        credito.setEstado(nuevoEstado);
        Credito guardado = creditoRepository.save(credito);

        logAuditoriaService.registrar(
                "CAMBIO_ESTADO_CREDITO",
                "Crédito #" + creditoId + " cambiado a " + nuevoEstado,
                credito.getUsuario().getId()
        );

        return guardado;
    }
}