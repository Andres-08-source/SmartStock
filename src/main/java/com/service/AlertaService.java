package com.service;

import com.model.Alerta;
import com.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    public List<Alerta> listarTodas() {
        return alertaRepository.findAll();
    }

    public List<Alerta> listarPendientes() {
        return alertaRepository.findByEstadoAlerta("Pendiente");
    }

    public Optional<Alerta> buscarPorId(Integer id) {
        return alertaRepository.findById(id);
    }

    public Alerta registrar(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    public Alerta actualizar(Integer id, Alerta datosNuevos) {
        return alertaRepository.findById(id).map(alertaExistente -> {
            alertaExistente.setEstadoAlerta(datosNuevos.getEstadoAlerta());
            alertaExistente.setDiasPrevios(datosNuevos.getDiasPrevios());
            return alertaRepository.save(alertaExistente);
        }).orElseThrow(() -> new RuntimeException("Alerta con id " + id + " no encontrada"));
    }

    public void eliminar(Integer id) {
        alertaRepository.deleteById(id);
    }
}