// Ubicación: src/test/java/com/service/LoteServiceTest.java
package com.service;

import com.model.Lote;
import com.model.Producto;
import com.repository.LoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoteServiceTest {

    @Mock
    private LoteRepository loteRepository;

    @InjectMocks
    private LoteService loteService;

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
    void listarTodos_debeRetornarTodosLosLotes() {
        Lote lote = new Lote(producto(), "L-001", 50, LocalDate.now().plusMonths(2));
        when(loteRepository.findAll()).thenReturn(List.of(lote));

        List<Lote> resultado = loteService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarVacio() {
        when(loteRepository.findById(50)).thenReturn(Optional.empty());

        assertTrue(loteService.buscarPorId(50).isEmpty());
    }

    @Test
    void registrar_debeGuardarElLote() {
        Lote nuevo = new Lote(producto(), "L-002", 30, LocalDate.now().plusMonths(1));
        when(loteRepository.save(nuevo)).thenReturn(nuevo);

        Lote resultado = loteService.registrar(nuevo);

        assertEquals("L-002", resultado.getNumeroLote());
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarCantidadYFecha() {
        Lote existente = new Lote(producto(), "L-001", 50, LocalDate.now().plusMonths(2));
        existente.setIdLote(1);
        LocalDate nuevaFecha = LocalDate.now().plusMonths(3);
        Lote datosNuevos = new Lote(producto(), "L-001", 40, nuevaFecha);

        when(loteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(loteRepository.save(any(Lote.class))).thenAnswer(inv -> inv.getArgument(0));

        Lote resultado = loteService.actualizar(1, datosNuevos);

        assertEquals(40, resultado.getCantidad());
        assertEquals(nuevaFecha, resultado.getFechaVencimiento());
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(loteRepository.findById(99)).thenReturn(Optional.empty());
        Lote datosNuevos = new Lote(producto(), "X", 1, LocalDate.now());

        assertThrows(RuntimeException.class, () -> loteService.actualizar(99, datosNuevos));
    }

    @Test
    void listarPorVencer_debeFiltrarPorFechaLimite() {
        LocalDate limite = LocalDate.now().plusDays(7);
        Lote proximoAVencer = new Lote(producto(), "L-003", 10, LocalDate.now().plusDays(3));
        when(loteRepository.findByFechaVencimientoBefore(limite)).thenReturn(List.of(proximoAVencer));

        List<Lote> resultado = loteService.listarPorVencer(limite);

        assertEquals(1, resultado.size());
        verify(loteRepository).findByFechaVencimientoBefore(limite);
    }

    // Nota: no existe un test de "eliminar" porque, según la matriz CRUD, LoteService
    // NO expone ningún método para eliminar lotes (se preserva la trazabilidad).
}