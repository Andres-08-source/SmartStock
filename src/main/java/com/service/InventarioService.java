package com.service;

import com.model.Inventario;
import com.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> listarTodos() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> buscarPorId(Integer id) {
        return inventarioRepository.findById(id);
    }

    public Inventario registrar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public Inventario actualizar(Integer id, Inventario datosNuevos) {
        return inventarioRepository.findById(id).map(inventarioExistente -> {
            inventarioExistente.setCantidadActual(datosNuevos.getCantidadActual());
            inventarioExistente.setCantidadMinima(datosNuevos.getCantidadMinima());
            return inventarioRepository.save(inventarioExistente);
        }).orElseThrow(() -> new RuntimeException("Inventario con id " + id + " no encontrado"));
    }

}