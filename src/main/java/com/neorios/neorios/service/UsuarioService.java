package com.neorios.neorios.service;

import java.time.LocalDateTime;
import java.util.List;
import com.neorios.neorios.model.Usuario;
import com.neorios.neorios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogAuditoriaService logAuditoriaService;

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        // Encriptamos la contraseña antes de guardarla
        String contrasenaEncriptada = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(contrasenaEncriptada);

        return usuarioRepository.save(usuario);
    
    }

    public Usuario login(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }

        return usuario;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario cambiarBloqueo(Long usuarioId, Boolean bloqueado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setBloqueado(bloqueado);
        Usuario guardado = usuarioRepository.save(usuario);

        logAuditoriaService.registrar(
                "CAMBIO_BLOQUEO_USUARIO",
                "Usuario #" + usuarioId + " bloqueado=" + bloqueado,
                usuarioId
        );

        return guardado;
    }

    public Usuario editarPerfil(Long usuarioId, String nuevoNombre, String nuevoCorreo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (nuevoCorreo != null && !nuevoCorreo.equals(usuario.getCorreo())) {
            if (usuarioRepository.findByCorreo(nuevoCorreo).isPresent()) {
                throw new RuntimeException("Ese correo ya está en uso por otro usuario");
            }
            usuario.setCorreo(nuevoCorreo);
        }

        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            usuario.setNombre(nuevoNombre);
        }

        return usuarioRepository.save(usuario);
    }

    public String solicitarRecuperacion(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese correo"));

        String codigo = String.valueOf(100000 + new java.util.Random().nextInt(900000));

        usuario.setCodigoRecuperacion(codigo);
        usuario.setCodigoExpiracion(LocalDateTime.now().plusMinutes(10));
        usuarioRepository.save(usuario);

        // Simulado: en un caso real, esto se enviaría por correo/SMS
        return "Código de recuperación generado: " + codigo + " (válido por 10 minutos)";
    }

    public Usuario restablecerContrasena(String correo, String codigo, String nuevaContrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getCodigoRecuperacion() == null || !usuario.getCodigoRecuperacion().equals(codigo)) {
            throw new RuntimeException("Código de recuperación incorrecto");
        }

        if (LocalDateTime.now().isAfter(usuario.getCodigoExpiracion())) {
            throw new RuntimeException("El código de recuperación expiró, solicita uno nuevo");
        }

        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuario.setCodigoRecuperacion(null);
        usuario.setCodigoExpiracion(null);

        return usuarioRepository.save(usuario);
    }
}