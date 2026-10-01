// Ubicación: src/test/java/com/service/MovimientoInventarioServiceTest.java
package com.service;

import com.model.Inventario;
import com.model.MovimientoInventario;
import com.model.Producto;
import com.repository.InventarioRepository;
import com.repository.MovimientoInventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Estas son las pruebas más importantes del proyecto: verifican que el
 * ajuste de stock (Inventario.cantidadActual) sea correcto para cada
 * tipo de movimiento, y que las reglas de negocio (no permitir stock
 * negativo, tipo inválido, producto sin inventario) se respeten.
 */
@ExtendWith(MockitoExtension.class)
class MovimientoInventarioServiceTest {

    @Mock
    private MovimientoInventarioRepository movimientoRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private MovimientoInventarioService movimientoService;

    private Producto producto;
    private Inventario inventario;

    @BeforeEach
    void setUp() {
        producto = new Producto(
                "Leche entera",
                "Leche x1L",
                "unidad",
                null,
                5000,
                10
        );

        producto.setIdProducto(1);

        inventario = new Inventario(producto, 50, 10);
    }

    @Test
    void registrar_entrada_debeIncrementarElStock() {
        MovimientoInventario movimiento = new MovimientoInventario(null, producto, "entrada", 20, "Reposición");

        when(inventarioRepository.findByProductoIdProducto(1)).thenReturn(Optional.of(inventario));
        when(movimientoRepository.save(movimiento)).thenReturn(movimiento);

        movimientoService.registrar(movimiento);

        assertEquals(70, inventario.getCantidadActual()); // 50 + 20
        verify(inventarioRepository).save(inventario);
        verify(movimientoRepository).save(movimiento);
    }

    @Test
    void registrar_salida_debeDecrementarElStock() {
        MovimientoInventario movimiento = new MovimientoInventario(null, producto, "salida", 15, "Venta");

        when(inventarioRepository.findByProductoIdProducto(1)).thenReturn(Optional.of(inventario));
        when(movimientoRepository.save(movimiento)).thenReturn(movimiento);

        movimientoService.registrar(movimiento);

        assertEquals(35, inventario.getCantidadActual()); // 50 - 15
    }

    @Test
    void registrar_salidaMayorAlStock_debeLanzarExcepcionYNoGuardarNada() {
        MovimientoInventario movimiento = new MovimientoInventario(null, producto, "salida", 999, "Venta enorme");

        when(inventarioRepository.findByProductoIdProducto(1)).thenReturn(Optional.of(inventario));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> movimientoService.registrar(movimiento));

        assertTrue(ex.getMessage().contains("Stock insuficiente"));
        // Ni el inventario ni el movimiento deben quedar guardados a medias
        verify(inventarioRepository, never()).save(any());
        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void registrar_ajuste_debeFijarLaCantidadDirectamente() {
        MovimientoInventario movimiento = new MovimientoInventario(null, producto, "ajuste", 100, "Conteo físico");

        when(inventarioRepository.findByProductoIdProducto(1)).thenReturn(Optional.of(inventario));
        when(movimientoRepository.save(movimiento)).thenReturn(movimiento);

        movimientoService.registrar(movimiento);

        assertEquals(100, inventario.getCantidadActual()); // el ajuste reemplaza, no suma
    }

    @Test
    void registrar_productoSinInventario_debeLanzarExcepcion() {
        MovimientoInventario movimiento = new MovimientoInventario(null, producto, "entrada", 10, "Ingreso");

        when(inventarioRepository.findByProductoIdProducto(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> movimientoService.registrar(movimiento));

        assertTrue(ex.getMessage().contains("no tiene inventario"));
    }

    @Test
    void registrar_tipoDeMovimientoInvalido_debeLanzarExcepcion() {
        MovimientoInventario movimiento = new MovimientoInventario(null, producto, "transferencia", 10, "Tipo raro");

        when(inventarioRepository.findByProductoIdProducto(1)).thenReturn(Optional.of(inventario));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> movimientoService.registrar(movimiento));

        assertTrue(ex.getMessage().contains("Tipo de movimiento inválido"));
    }
}