// Ubicación: src/test/java/com/service/UsuarioServiceTest.java
package com.service;

import com.model.Rol;
import com.model.Usuario;
import com.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Rol rolUsuario() {
        Rol rol = new Rol("Usuario", "Rol por defecto");
        rol.setIdRol(2);
        return rol;
    }

    @Test
    void listarTodos_debeRetornarTodosLosUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(
                new Usuario("Bairon", "Perez", "bairon@correo.com", "HASH1", rolUsuario()),
                new Usuario("Andres", "Leguizamon", "andres@correo.com", "HASH2", rolUsuario())));

        List<Usuario> resultado = usuarioService.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarVacio() {
        when(usuarioRepository.findById(50)).thenReturn(Optional.empty());

        assertTrue(usuarioService.buscarPorId(50).isEmpty());
    }

    @Test
    void registrar_debeGuardarElUsuario() {
        Usuario nuevo = new Usuario("Bairon", "Perez", "bairon@correo.com", "HASH1", rolUsuario());
        when(usuarioRepository.save(nuevo)).thenReturn(nuevo);

        Usuario resultado = usuarioService.registrar(nuevo);

        assertEquals("bairon@correo.com", resultado.getCorreo());
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarCamposIncluidoRol() {
        Usuario existente = new Usuario("Bairon", "Perez", "bairon@correo.com", "HASH1", rolUsuario());
        existente.setIdUsuario(1);

        Rol nuevoRol = new Rol("Administrador", "Acceso total");
        nuevoRol.setIdRol(1);
        Usuario datosNuevos = new Usuario("Bairon", "Perez Actualizado", "bairon2@correo.com", "HASH1", nuevoRol);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = usuarioService.actualizar(1, datosNuevos);

        assertEquals("Perez Actualizado", resultado.getApellido());
        assertEquals("bairon2@correo.com", resultado.getCorreo());
        assertEquals(1, resultado.getRol().getIdRol());
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());
        Usuario datosNuevos = new Usuario("X", "Y", "x@correo.com", "HASH", rolUsuario());

        assertThrows(RuntimeException.class, () -> usuarioService.actualizar(99, datosNuevos));
    }

    @Test
    void eliminar_debeInvocarDeleteByIdConElIdCorrecto() {
        usuarioService.eliminar(4);
        verify(usuarioRepository, times(1)).deleteById(4);
    }
}