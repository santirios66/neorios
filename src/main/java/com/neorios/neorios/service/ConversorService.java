package com.neorios.neorios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ConversorService {

    @Autowired
    private RestTemplate restTemplate;

    public Double convertir(String monedaOrigen, String monedaDestino, Double monto) {
        String url = "https://open.er-api.com/v6/latest/" + monedaOrigen;

        Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);

        if (respuesta == null || !"success".equals(respuesta.get("result"))) {
            throw new RuntimeException("No se pudo obtener la tasa de cambio");
        }

        Map<String, Object> tasas = (Map<String, Object>) respuesta.get("rates");
        Object tasaObj = tasas.get(monedaDestino);

        if (tasaObj == null) {
            throw new RuntimeException("Moneda destino no soportada: " + monedaDestino);
        }

        Double tasa = Double.valueOf(tasaObj.toString());
        double resultado = monto * tasa;

        return Math.round(resultado * 100.0) / 100.0;
    }
}