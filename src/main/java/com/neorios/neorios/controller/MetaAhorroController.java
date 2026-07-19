package com.neorios.neorios.controller;

import com.neorios.neorios.model.MetaAhorro;
import com.neorios.neorios.service.MetaAhorroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metas")
public class MetaAhorroController {

    @Autowired
    private MetaAhorroService metaAhorroService;

    @PostMapping("/crear/{usuarioId}")
    public MetaAhorro crear(@PathVariable Long usuarioId,
                             @RequestParam String nombre,
                             @RequestParam Double montoObjetivo) {
        return metaAhorroService.crearMeta(usuarioId, nombre, montoObjetivo);
    }

    @PutMapping("/{metaId}/abonar")
    public MetaAhorro abonar(@PathVariable Long metaId, @RequestParam Double monto) {
        return metaAhorroService.abonar(metaId, monto);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<MetaAhorro> listar(@PathVariable Long usuarioId) {
        return metaAhorroService.listarMetasDeUsuario(usuarioId);
    }
}