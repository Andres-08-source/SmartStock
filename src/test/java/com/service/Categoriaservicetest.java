// Ubicación: src/test/java/com/service/CategoriaServiceTest.java
package com.service;

import com.model.Categoria;
import com.repository.CategoriaRepository;
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

/**
 * Pruebas unitarias de CategoriaService.
 * Se usa Mockito para "simular" CategoriaRepository: así probamos SOLO
 * la lógica del Service, sin necesitar una base de datos real.
 */
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void listarTodas_debeRetornarTodasLasCategorias() {
        Categoria c1 = new Categoria("Lácteos", "Productos lácteos");
        Categoria c2 = new Categoria("Bebidas", "Bebidas en general");
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Categoria> resultado = categoriaService.listarTodas();

        assertEquals(2, resultado.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarLaCategoria() {
        Categoria categoria = new Categoria("Lácteos", "Productos lácteos");
        categoria.setIdCategoria(1);
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        Optional<Categoria> resultado = categoriaService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Lácteos", resultado.get().getNombreCategoria());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarVacio() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = categoriaService.buscarPorId(99);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void registrar_debeGuardarLaCategoria() {
        Categoria nueva = new Categoria("Aseo", "Productos de aseo");
        when(categoriaRepository.save(nueva)).thenReturn(nueva);

        Categoria resultado = categoriaService.registrar(nueva);

        assertEquals("Aseo", resultado.getNombreCategoria());
        verify(categoriaRepository).save(nueva);
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarNombreYDescripcion() {
        Categoria existente = new Categoria("Lácteos", "Vieja descripción");
        existente.setIdCategoria(1);
        Categoria datosNuevos = new Categoria("Lácteos y derivados", "Nueva descripción");

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));

        Categoria resultado = categoriaService.actualizar(1, datosNuevos);

        assertEquals("Lácteos y derivados", resultado.getNombreCategoria());
        assertEquals("Nueva descripción", resultado.getDescripcion());
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());
        Categoria datosNuevos = new Categoria("X", "Y");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> categoriaService.actualizar(99, datosNuevos));

        assertTrue(ex.getMessage().contains("no encontrada"));
    }

    @Test
    void eliminar_debeInvocarDeleteByIdConElIdCorrecto() {
        categoriaService.eliminar(5);
        verify(categoriaRepository, times(1)).deleteById(5);
    }
}