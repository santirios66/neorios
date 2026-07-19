package com.neorios.neorios.service;

import com.neorios.neorios.model.Cuenta;
import com.neorios.neorios.model.Transaccion;
import com.neorios.neorios.repository.CuentaRepository;
import com.neorios.neorios.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransaccionService {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Transactional
    public Transaccion transferir(String numeroCuentaOrigen, String numeroCuentaDestino, Double monto) {

        if (monto <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }

        Cuenta origen = cuentaRepository.findByNumeroCuenta(numeroCuentaOrigen)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada"));

        Cuenta destino = cuentaRepository.findByNumeroCuenta(numeroCuentaDestino)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada"));

        if (origen.getUsuario().getBloqueado()) {
            throw new RuntimeException("No se puede transferir: el usuario origen está bloqueado");
        }

        if (destino.getUsuario().getBloqueado()) {
            throw new RuntimeException("No se puede transferir: el usuario destino está bloqueado");
        }

        if (origen.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente");
        }

        origen.setSaldo(origen.getSaldo() - monto);
        destino.setSaldo(destino.getSaldo() + monto);

        cuentaRepository.save(origen);
        cuentaRepository.save(destino);

        Transaccion transaccion = new Transaccion();
        transaccion.setCuentaOrigen(origen);
        transaccion.setCuentaDestino(destino);
        transaccion.setMonto(monto);
        transaccion.setFecha(LocalDateTime.now());

        notificacionService.crearNotificacion(origen.getUsuario(),
                "Enviaste $" + monto + " a la cuenta " + destino.getNumeroCuenta());

        notificacionService.crearNotificacion(destino.getUsuario(),
                "Recibiste $" + monto + " de la cuenta " + origen.getNumeroCuenta());

        return transaccionRepository.save(transaccion);
    }

    public List<Transaccion> obtenerHistorial(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        return transaccionRepository.findByCuentaOrigenIdOrCuentaDestinoId(cuenta.getId(), cuenta.getId());
    }

    @Transactional
    public Transaccion revertirTransaccion(Long transaccionId) {
        Transaccion original = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        LocalDateTime limite = original.getFecha().plusMinutes(15);
        if (LocalDateTime.now().isAfter(limite)) {
            throw new RuntimeException("No se puede revertir: pasaron más de 15 minutos desde la transacción");
        }

        Cuenta origen = original.getCuentaOrigen();
        Cuenta destino = original.getCuentaDestino();
        Double monto = original.getMonto();

        if (destino.getSaldo() < monto) {
            throw new RuntimeException("No se puede revertir: la cuenta destino ya no tiene saldo suficiente");
        }

        origen.setSaldo(origen.getSaldo() + monto);
        destino.setSaldo(destino.getSaldo() - monto);

        cuentaRepository.save(origen);
        cuentaRepository.save(destino);

        Transaccion reversion = new Transaccion();
        reversion.setCuentaOrigen(destino);
        reversion.setCuentaDestino(origen);
        reversion.setMonto(monto);
        reversion.setFecha(LocalDateTime.now());

        notificacionService.crearNotificacion(origen.getUsuario(),
                "Se revirtió una transferencia: recuperaste $" + monto);

        notificacionService.crearNotificacion(destino.getUsuario(),
                "Se revirtió una transferencia: se descontaron $" + monto);

        return transaccionRepository.save(reversion);
    }

    public String generarComprobante(Long transaccionId) {
        Transaccion t = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        return "===== COMPROBANTE DE TRANSACCIÓN - NEORÍOS =====\n" +
                "N° Transacción: " + t.getId() + "\n" +
                "Fecha: " + t.getFecha() + "\n" +
                "Cuenta origen: " + t.getCuentaOrigen().getNumeroCuenta() +
                " (" + t.getCuentaOrigen().getUsuario().getNombre() + ")\n" +
                "Cuenta destino: " + t.getCuentaDestino().getNumeroCuenta() +
                " (" + t.getCuentaDestino().getUsuario().getNombre() + ")\n" +
                "Monto: $" + t.getMonto() + "\n" +
                "==============================================";
    }

    public byte[] generarExtractoPdf(String numeroCuenta) throws IOException {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        List<Transaccion> movimientos = transaccionRepository
                .findByCuentaOrigenIdOrCuentaDestinoId(cuenta.getId(), cuenta.getId());

        PDDocument documento = new PDDocument();
        PDPage pagina = new PDPage();
        documento.addPage(pagina);

        PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

        float y = 750;
        contenido.beginText();
        contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
        contenido.newLineAtOffset(50, y);
        contenido.showText("EXTRACTO BANCARIO - NEORIOS");
        contenido.endText();

        y -= 30;
        contenido.beginText();
        contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
        contenido.newLineAtOffset(50, y);
        contenido.showText("Cuenta: " + cuenta.getNumeroCuenta());
        contenido.endText();

        y -= 15;
        contenido.beginText();
        contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
        contenido.newLineAtOffset(50, y);
        contenido.showText("Titular: " + cuenta.getUsuario().getNombre());
        contenido.endText();

        y -= 15;
        contenido.beginText();
        contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
        contenido.newLineAtOffset(50, y);
        contenido.showText("Saldo actual: $" + cuenta.getSaldo());
        contenido.endText();

        y -= 30;

        for (Transaccion t : movimientos) {
            boolean esOrigen = t.getCuentaOrigen().getId().equals(cuenta.getId());
            String tipo = esOrigen ? "SALIDA" : "ENTRADA";
            String contraparte = esOrigen
                    ? t.getCuentaDestino().getNumeroCuenta()
                    : t.getCuentaOrigen().getNumeroCuenta();

            String linea = t.getFecha() + " | " + tipo + " | $" + t.getMonto() + " | cuenta: " + contraparte;

            contenido.beginText();
            contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
            contenido.newLineAtOffset(50, y);
            contenido.showText(linea);
            contenido.endText();

            y -= 15;
        }

        contenido.close();

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        documento.save(salida);
        documento.close();

        return salida.toByteArray();
    }
}