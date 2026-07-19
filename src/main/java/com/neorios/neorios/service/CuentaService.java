package com.neorios.neorios.service;

import com.neorios.neorios.model.Cuenta;
import com.neorios.neorios.model.Usuario;
import com.neorios.neorios.repository.CuentaRepository;
import com.neorios.neorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Cuenta abrirCuenta(Long usuarioId, String tipoCuenta) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(usuario);
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setSaldo(0.0);
        cuenta.setNumeroCuenta(generarNumeroCuenta());

        return cuentaRepository.save(cuenta);
    }

    public List<Cuenta> listarCuentasDeUsuario(Long usuarioId) {
        return cuentaRepository.findByUsuarioId(usuarioId);
    }

    public void cerrarCuenta(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (cuenta.getSaldo() > 0) {
            throw new RuntimeException("No se puede cerrar la cuenta: aún tiene saldo. Retira o transfiere el dinero primero");
        }

        cuentaRepository.delete(cuenta);
    }

    private String generarNumeroCuenta() {
        Random random = new Random();
        long numero = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(numero);
    }
}