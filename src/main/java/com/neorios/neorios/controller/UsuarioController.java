package com.neorios.neorios.controller;

import java.util.List;
import com.neorios.neorios.model.Usuario;
import com.neorios.neorios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public Usuario registrar(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario datosLogin) {
        return usuarioService.login(datosLogin.getCorreo(), datosLogin.getContrasena());
    }

    @GetMapping("/todos")
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @PutMapping("/{usuarioId}/bloqueo")
    public Usuario cambiarBloqueo(@PathVariable Long usuarioId, @RequestParam Boolean bloqueado) {
        return usuarioService.cambiarBloqueo(usuarioId, bloqueado);
    }

    @PutMapping("/{usuarioId}/perfil")
    public Usuario editarPerfil(@PathVariable Long usuarioId,
                                  @RequestParam(required = false) String nombre,
                                  @RequestParam(required = false) String correo) {
        return usuarioService.editarPerfil(usuarioId, nombre, correo);
    }

    @PostMapping("/recuperar")
    public String solicitarRecuperacion(@RequestParam String correo) {
        return usuarioService.solicitarRecuperacion(correo);
    }

    @PostMapping("/restablecer")
    public Usuario restablecerContrasena(@RequestParam String correo,
                                           @RequestParam String codigo,
                                           @RequestParam String nuevaContrasena) {
        return usuarioService.restablecerContrasena(correo, codigo, nuevaContrasena);
    }
}