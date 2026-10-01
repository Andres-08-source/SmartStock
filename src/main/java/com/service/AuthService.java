package com.service;

import com.dto.AuthRequest;
import com.dto.AuthResponse;
import com.model.Usuario;
import com.repository.RolRepository;
import com.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Integer ID_ROL_USUARIO_POR_DEFECTO = 2;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse registrar(AuthRequest request) {
        Optional<Usuario> existente = usuarioRepository.findByCorreo(request.getCorreo());
        if (existente.isPresent()) {
            return new AuthResponse(false, "Este correo ya está registrado");
        }

        String contrasenaEncriptada = passwordEncoder.encode(request.getContrasena());

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setApellido(request.getApellido());
        nuevoUsuario.setCorreo(request.getCorreo());
        nuevoUsuario.setContrasena(contrasenaEncriptada);

        rolRepository.findById(ID_ROL_USUARIO_POR_DEFECTO).ifPresent(nuevoUsuario::setRol);

        usuarioRepository.save(nuevoUsuario);

        return new AuthResponse(true, "Usuario registrado exitosamente");
    }

    public AuthResponse login(AuthRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(request.getCorreo());

        if (usuarioOpt.isEmpty()) {
            return new AuthResponse(false, "Error en la autenticación: usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        boolean coincide = passwordEncoder.matches(request.getContrasena(), usuario.getContrasena());

        if (coincide) {
            return new AuthResponse(true, "Autenticación satisfactoria");
        } else {
            return new AuthResponse(false, "Error en la autenticación: contraseña incorrecta");
        }
    }
}