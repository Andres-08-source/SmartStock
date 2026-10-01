// Ubicación: src/test/java/com/service/AlertaServiceTest.java
package com.service;

import com.model.Alerta;
import com.model.Lote;
import com.repository.AlertaRepository;
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
class AlertaServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private AlertaService alertaService;

    private Lote lote() {
        Lote l = new Lote(null, "L-001", 50, null);
        l.setIdLote(1);
        return l;
    }

    @Test
    void listarTodas_debeRetornarTodasLasAlertas() {
        when(alertaRepository.findAll()).thenReturn(List.of(new Alerta(lote(), 3)));

        List<Alerta> resultado = alertaService.listarTodas();

        assertEquals(1, resultado.size());
    }

    @Test
    void listarPendientes_debeFiltrarPorEstadoPendiente() {
        Alerta pendiente = new Alerta(lote(), 3);
        when(alertaRepository.findByEstadoAlerta("Pendiente")).thenReturn(List.of(pendiente));

        List<Alerta> resultado = alertaService.listarPendientes();

        assertEquals(1, resultado.size());
        assertEquals("Pendiente", resultado.get(0).getEstadoAlerta());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarVacio() {
        when(alertaRepository.findById(50)).thenReturn(Optional.empty());

        assertTrue(alertaService.buscarPorId(50).isEmpty());
    }

    @Test
    void registrar_debeGuardarLaAlertaConEstadoPendientePorDefecto() {
        Alerta nueva = new Alerta(lote(), 5);
        when(alertaRepository.save(nueva)).thenReturn(nueva);

        Alerta resultado = alertaService.registrar(nueva);

        assertEquals("Pendiente", resultado.getEstadoAlerta());
        assertEquals(5, resultado.getDiasPrevios());
    }

    @Test
    void actualizar_debeCambiarEstadoDePendienteAAtendida() {
        Alerta existente = new Alerta(lote(), 3);
        existente.setIdAlerta(1);
        Alerta datosNuevos = new Alerta(lote(), 3);
        datosNuevos.setEstadoAlerta("Atendida");

        when(alertaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(alertaRepository.save(any(Alerta.class))).thenAnswer(inv -> inv.getArgument(0));

        Alerta resultado = alertaService.actualizar(1, datosNuevos);

        assertEquals("Atendida", resultado.getEstadoAlerta());
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(alertaRepository.findById(99)).thenReturn(Optional.empty());
        Alerta datosNuevos = new Alerta(lote(), 1);

        assertThrows(RuntimeException.class, () -> alertaService.actualizar(99, datosNuevos));
    }

    @Test
    void eliminar_debeInvocarDeleteByIdConElIdCorrecto() {
        alertaService.eliminar(7);
        verify(alertaRepository, times(1)).deleteById(7);
    }
}