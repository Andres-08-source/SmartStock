package com.service;

import com.model.Categoria;
import com.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria registrar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Integer id, Categoria datosNuevos) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            categoriaExistente.setNombreCategoria(datosNuevos.getNombreCategoria());
            categoriaExistente.setDescripcion(datosNuevos.getDescripcion());
            return categoriaRepository.save(categoriaExistente);
        }).orElseThrow(() -> new RuntimeException("Categoría con id " + id + " no encontrada"));
    }

    public void eliminar(Integer id) {
        categoriaRepository.deleteById(id);
    }
}