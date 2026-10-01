// Ubicación: src/test/java/com/service/RolServiceTest.java
package com.service;

import com.model.Rol;
import com.repository.RolRepository;
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
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    @Test
    void listarTodos_debeRetornarTodosLosRoles() {
        when(rolRepository.findAll()).thenReturn(Arrays.asList(
                new Rol("Administrador", "Acceso total"),
                new Rol("Usuario", "Rol por defecto")));

        List<Rol> resultado = rolService.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarElRol() {
        Rol rol = new Rol("Usuario", "Rol por defecto");
        rol.setIdRol(2);
        when(rolRepository.findById(2)).thenReturn(Optional.of(rol));

        Optional<Rol> resultado = rolService.buscarPorId(2);

        assertTrue(resultado.isPresent());
        assertEquals("Usuario", resultado.get().getNombreRol());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarVacio() {
        when(rolRepository.findById(99)).thenReturn(Optional.empty());

        assertTrue(rolService.buscarPorId(99).isEmpty());
    }

    @Test
    void registrar_debeGuardarElRol() {
        Rol nuevo = new Rol("Supervisor", "Supervisa bodega");
        when(rolRepository.save(nuevo)).thenReturn(nuevo);

        Rol resultado = rolService.registrar(nuevo);

        assertEquals("Supervisor", resultado.getNombreRol());
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarNombreYDescripcion() {
        Rol existente = new Rol("Usuario", "Descripción vieja");
        existente.setIdRol(2);
        Rol datosNuevos = new Rol("Usuario estándar", "Descripción nueva");

        when(rolRepository.findById(2)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(Rol.class))).thenAnswer(inv -> inv.getArgument(0));

        Rol resultado = rolService.actualizar(2, datosNuevos);

        assertEquals("Usuario estándar", resultado.getNombreRol());
        assertEquals("Descripción nueva", resultado.getDescripcion());
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(rolRepository.findById(99)).thenReturn(Optional.empty());
        Rol datosNuevos = new Rol("X", "Y");

        assertThrows(RuntimeException.class, () -> rolService.actualizar(99, datosNuevos));
    }

    @Test
    void eliminar_debeInvocarDeleteByIdConElIdCorrecto() {
        rolService.eliminar(1);
        verify(rolRepository, times(1)).deleteById(1);
    }
}