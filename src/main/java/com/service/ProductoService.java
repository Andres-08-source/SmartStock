package com.service;

import com.model.Categoria;
import com.model.Producto;
import com.repository.CategoriaRepository;
import com.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    public List<Producto> listarPorCategoria(Integer idCategoria) {
        return productoRepository.findByCategoriaIdCategoria(idCategoria);
    }

    public Producto registrar(Producto producto) {

        Categoria categoria = categoriaRepository.findById(
                producto.getCategoria().getIdCategoria()
        ).orElseThrow(() ->
                new RuntimeException("Categoría no encontrada")
        );

        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, Producto datosNuevos) {
        return productoRepository.findById(id).map(productoExistente -> {
            productoExistente.setNombreProducto(datosNuevos.getNombreProducto());
            productoExistente.setDescripcion(datosNuevos.getDescripcion());
            productoExistente.setUnidadMedida(datosNuevos.getUnidadMedida());
            productoExistente.setCategoria(datosNuevos.getCategoria());
            productoExistente.setPrecio(datosNuevos.getPrecio());
            productoExistente.setStockMinimo(datosNuevos.getStockMinimo());
            return productoRepository.save(productoExistente);
        }).orElseThrow(() -> new RuntimeException("Producto con id " + id + " no encontrado"));
    }

    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }
}