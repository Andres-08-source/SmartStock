// Ubicación: src/test/java/com/service/AuthServiceTest.java
package com.service;

import com.dto.AuthRequest;
import com.dto.AuthResponse;
import com.model.Rol;
import com.model.Usuario;
import com.repository.RolRepository;
import com.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas del componente de seguridad: registro y autenticación.
 * Se mockea PasswordEncoder para no depender de BCrypt real en los tests
 * (más rápido y más fácil de controlar el resultado esperado).
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void registrar_correoNuevo_debeCrearUsuarioExitosamente() {
        AuthRequest request = new AuthRequest();
        request.setNombre("Bairon");
        request.setApellido("Perez");
        request.setCorreo("bairon@correo.com");
        request.setContrasena("clave123");

        Rol rolUsuario = new Rol("Usuario", "Rol por defecto");
        rolUsuario.setIdRol(2);

        when(usuarioRepository.findByCorreo("bairon@correo.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("clave123")).thenReturn("HASH_SIMULADO");
        when(rolRepository.findById(2)).thenReturn(Optional.of(rolUsuario));

        AuthResponse response = authService.registrar(request);

        assertTrue(response.isAutenticado());
        assertEquals("Usuario registrado exitosamente", response.getMensaje());
        verify(usuarioRepository).save(argThat(u ->
                u.getContrasena().equals("HASH_SIMULADO") && u.getRol().getIdRol() == 2));
    }

    @Test
    void registrar_correoYaExiste_debeRetornarError() {
        AuthRequest request = new AuthRequest();
        request.setCorreo("bairon@correo.com");
        request.setContrasena("clave123");

        when(usuarioRepository.findByCorreo("bairon@correo.com"))
                .thenReturn(Optional.of(new Usuario()));

        AuthResponse response = authService.registrar(request);

        assertFalse(response.isAutenticado());
        assertEquals("Este correo ya está registrado", response.getMensaje());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void login_credencialesCorrectas_debeRetornarExito() {
        Usuario usuario = new Usuario("Bairon", "Perez", "bairon@correo.com", "HASH_REAL", null);

        AuthRequest request = new AuthRequest();
        request.setCorreo("bairon@correo.com");
        request.setContrasena("clave123");

        when(usuarioRepository.findByCorreo("bairon@correo.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("clave123", "HASH_REAL")).thenReturn(true);

        AuthResponse response = authService.login(request);

        assertTrue(response.isAutenticado());
        assertEquals("Autenticación satisfactoria", response.getMensaje());
    }

    @Test
    void login_usuarioNoExiste_debeRetornarError() {
        AuthRequest request = new AuthRequest();
        request.setCorreo("noexiste@correo.com");
        request.setContrasena("clave123");

        when(usuarioRepository.findByCorreo("noexiste@correo.com")).thenReturn(Optional.empty());

        AuthResponse response = authService.login(request);

        assertFalse(response.isAutenticado());
        assertTrue(response.getMensaje().contains("usuario no encontrado"));
    }

    @Test
    void login_contrasenaIncorrecta_debeRetornarError() {
        Usuario usuario = new Usuario("Bairon", "Perez", "bairon@correo.com", "HASH_REAL", null);

        AuthRequest request = new AuthRequest();
        request.setCorreo("bairon@correo.com");
        request.setContrasena("claveIncorrecta");

        when(usuarioRepository.findByCorreo("bairon@correo.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("claveIncorrecta", "HASH_REAL")).thenReturn(false);

        AuthResponse response = authService.login(request);

        assertFalse(response.isAutenticado());
        assertTrue(response.getMensaje().contains("contraseña incorrecta"));
    }
}