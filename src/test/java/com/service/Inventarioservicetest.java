// Ubicación: src/test/java/com/service/InventarioServiceTest.java
package com.service;

import com.model.Inventario;
import com.model.Producto;
import com.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Producto producto() {
        Producto p = new Producto(
                "Leche entera",
                "Leche x1L",
                "unidad",
                null,
                5000,
                10
        );
        p.setIdProducto(1);
        return p;
    }

    @Test
    void listarTodos_debeRetornarTodosLosInventarios() {
        Inventario inv = new Inventario(producto(), 50, 10);
        when(inventarioRepository.findAll()).thenReturn(List.of(inv));

        List<Inventario> resultado = inventarioService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarVacio() {
        when(inventarioRepository.findById(50)).thenReturn(Optional.empty());

        assertTrue(inventarioService.buscarPorId(50).isEmpty());
    }

    @Test
    void registrar_debeGuardarElInventario() {
        Inventario nuevo = new Inventario(producto(), 100, 20);
        when(inventarioRepository.save(nuevo)).thenReturn(nuevo);

        Inventario resultado = inventarioService.registrar(nuevo);

        assertEquals(100, resultado.getCantidadActual());
        assertEquals(20, resultado.getCantidadMinima());
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarCantidades() {
        Inventario existente = new Inventario(producto(), 50, 10);
        existente.setIdInventario(1);
        Inventario datosNuevos = new Inventario(producto(), 80, 15);

        when(inventarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(inv -> inv.getArgument(0));

        Inventario resultado = inventarioService.actualizar(1, datosNuevos);

        assertEquals(80, resultado.getCantidadActual());
        assertEquals(15, resultado.getCantidadMinima());
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(inventarioRepository.findById(99)).thenReturn(Optional.empty());
        Inventario datosNuevos = new Inventario(producto(), 1, 1);

        assertThrows(RuntimeException.class, () -> inventarioService.actualizar(99, datosNuevos));
    }

    // Nota: no existe un test de "eliminar" porque, según la matriz CRUD, InventarioService
    // NO expone ningún método para eliminar inventarios.
}