// Ubicación: src/main/java/com/service/LoteService.java
package com.service;

import com.model.Lote;
import com.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoteService {

    @Autowired
    private LoteRepository loteRepository;

    public List<Lote> listarTodos() {
        return loteRepository.findAll();
    }

    public Optional<Lote> buscarPorId(Integer id) {
        return loteRepository.findById(id);
    }

    public Lote registrar(Lote lote) {
        return loteRepository.save(lote);
    }

    public Lote actualizar(Integer id, Lote datosNuevos) {
        return loteRepository.findById(id).map(loteExistente -> {
            loteExistente.setCantidad(datosNuevos.getCantidad());
            loteExistente.setFechaVencimiento(datosNuevos.getFechaVencimiento());
            return loteRepository.save(loteExistente);
        }).orElseThrow(() -> new RuntimeException("Lote con id " + id + " no encontrado"));
    }

    public List<Lote> listarPorVencer(LocalDate fechaLimite) {
        return loteRepository.findByFechaVencimientoBefore(fechaLimite);
    }
}