// Ubicación: src/test/java/com/service/NotificacionServiceTest.java
package com.service;

import com.model.Alerta;
import com.model.Lote;
import com.model.Notificacion;
import com.model.Usuario;
import com.repository.NotificacionRepository;
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
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Usuario usuario() {
        Usuario u = new Usuario("Bairon", "Perez", "bairon@correo.com", "HASH", null);
        u.setIdUsuario(1);
        return u;
    }

    private Alerta alerta() {
        Lote lote = new Lote(null, "L-001", 50, null);
        lote.setIdLote(1);
        Alerta a = new Alerta(lote, 3);
        a.setIdAlerta(1);
        return a;
    }

    @Test
    void listarTodas_debeRetornarTodasLasNotificaciones() {
        when(notificacionRepository.findAll()).thenReturn(
                List.of(new Notificacion(usuario(), alerta(), "El lote L-001 vence pronto")));

        List<Notificacion> resultado = notificacionService.listarTodas();

        assertEquals(1, resultado.size());
    }

    @Test
    void listarNoLeidasPorUsuario_debeFiltrarPorUsuarioYNoLeidas() {
        Notificacion noLeida = new Notificacion(usuario(), alerta(), "El lote L-001 vence pronto");
        when(notificacionRepository.findByUsuarioIdUsuarioAndLeidaFalse(1)).thenReturn(List.of(noLeida));

        List<Notificacion> resultado = notificacionService.listarNoLeidasPorUsuario(1);

        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).isLeida());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarVacio() {
        when(notificacionRepository.findById(50)).thenReturn(Optional.empty());

        assertTrue(notificacionService.buscarPorId(50).isEmpty());
    }

    @Test
    void registrar_debeGuardarLaNotificacionComoNoLeidaPorDefecto() {
        Notificacion nueva = new Notificacion(usuario(), alerta(), "El lote L-001 vence pronto");
        when(notificacionRepository.save(nueva)).thenReturn(nueva);

        Notificacion resultado = notificacionService.registrar(nueva);

        assertFalse(resultado.isLeida());
        assertEquals("El lote L-001 vence pronto", resultado.getMensaje());
    }

    @Test
    void actualizar_debeMarcarLaNotificacionComoLeida() {
        Notificacion existente = new Notificacion(usuario(), alerta(), "El lote L-001 vence pronto");
        existente.setIdNotificacion(1);
        Notificacion datosNuevos = new Notificacion(usuario(), alerta(), "El lote L-001 vence pronto");
        datosNuevos.setLeida(true);

        when(notificacionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(inv -> inv.getArgument(0));

        Notificacion resultado = notificacionService.actualizar(1, datosNuevos);

        assertTrue(resultado.isLeida());
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(notificacionRepository.findById(99)).thenReturn(Optional.empty());
        Notificacion datosNuevos = new Notificacion(usuario(), alerta(), "X");

        assertThrows(RuntimeException.class, () -> notificacionService.actualizar(99, datosNuevos));
    }

    @Test
    void eliminar_debeInvocarDeleteByIdConElIdCorrecto() {
        notificacionService.eliminar(3);
        verify(notificacionRepository, times(1)).deleteById(3);
    }
}