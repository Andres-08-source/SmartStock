// Ubicación: src/test/java/com/service/ProductoServiceTest.java
package com.service;

import com.model.Categoria;
import com.model.Producto;
import com.repository.CategoriaRepository;
import com.repository.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    // registrar() valida que la categoría exista antes de guardar,
    // así que hay que simular también el CategoriaRepository.
    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Categoria categoriaLacteos() {
        Categoria categoria = new Categoria("Lácteos", "Productos lácteos");
        categoria.setIdCategoria(1);
        return categoria;
    }

    // Constructor real: (nombreProducto, descripcion, unidadMedida, categoria, precio, stockMinimo)
    private Producto producto(String nombre, String descripcion, String unidad, Categoria categoria, int precio, int stockMinimo) {
        return new Producto(nombre, descripcion, unidad, categoria, precio, stockMinimo);
    }

    @Test
    void listarTodos_debeRetornarTodosLosProductos() {
        Producto p1 = producto("Leche entera", "Leche x1L", "unidad", categoriaLacteos(), 4500, 10);
        Producto p2 = producto("Yogurt", "Yogurt x1L", "unidad", categoriaLacteos(), 3200, 15);
        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = productoService.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void listarPorCategoria_debeFiltrarPorIdCategoria() {
        Producto p1 = producto("Leche entera", "Leche x1L", "unidad", categoriaLacteos(), 4500, 10);
        when(productoRepository.findByCategoriaIdCategoria(1)).thenReturn(List.of(p1));

        List<Producto> resultado = productoService.listarPorCategoria(1);

        assertEquals(1, resultado.size());
        verify(productoRepository).findByCategoriaIdCategoria(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarVacio() {
        when(productoRepository.findById(50)).thenReturn(Optional.empty());

        assertTrue(productoService.buscarPorId(50).isEmpty());
    }

    @Test
    void registrar_cuandoLaCategoriaExiste_debeGuardarElProducto() {
        Producto nuevo = producto("Queso campesino", "Queso x500g", "unidad", categoriaLacteos(), 8900, 5);

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaLacteos()));
        when(productoRepository.save(nuevo)).thenReturn(nuevo);

        Producto resultado = productoService.registrar(nuevo);

        assertEquals("Queso campesino", resultado.getNombreProducto());
        assertEquals("Lácteos", resultado.getCategoria().getNombreCategoria());
        assertEquals(8900, resultado.getPrecio());
        assertEquals(5, resultado.getStockMinimo());
    }

    @Test
    void registrar_cuandoLaCategoriaNoExiste_debeLanzarExcepcionYNoGuardar() {
        Producto nuevo = producto("Queso campesino", "Queso x500g", "unidad", categoriaLacteos(), 8900, 5);

        when(categoriaRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> productoService.registrar(nuevo));

        assertTrue(ex.getMessage().contains("Categoría no encontrada"));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarCamposIncluidaCategoria() {
        Producto existente = producto("Leche", "Vieja desc", "unidad", categoriaLacteos(), 4500, 10);
        existente.setIdProducto(1);

        Categoria nuevaCategoria = new Categoria("Bebidas", "Bebidas en general");
        nuevaCategoria.setIdCategoria(2);
        Producto datosNuevos = producto("Leche deslactosada", "Nueva desc", "litro", nuevaCategoria, 5200, 8);

        when(productoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto resultado = productoService.actualizar(1, datosNuevos);

        assertEquals("Leche deslactosada", resultado.getNombreProducto());
        assertEquals("litro", resultado.getUnidadMedida());
        assertEquals(2, resultado.getCategoria().getIdCategoria());
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());
        Producto datosNuevos = producto("X", "Y", "unidad", categoriaLacteos(), 1000, 1);

        assertThrows(RuntimeException.class, () -> productoService.actualizar(99, datosNuevos));
    }

    @Test
    void eliminar_debeInvocarDeleteByIdConElIdCorrecto() {
        productoService.eliminar(3);
        verify(productoRepository, times(1)).deleteById(3);
    }
}