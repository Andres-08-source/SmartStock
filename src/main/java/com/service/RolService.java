package com.service;

import com.model.Rol;
import com.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    public Optional<Rol> buscarPorId(Integer id) {
        return rolRepository.findById(id);
    }

    public Rol registrar(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol actualizar(Integer id, Rol datosNuevos) {
        return rolRepository.findById(id).map(rolExistente -> {
            rolExistente.setNombreRol(datosNuevos.getNombreRol());
            rolExistente.setDescripcion(datosNuevos.getDescripcion());
            return rolRepository.save(rolExistente);
        }).orElseThrow(() -> new RuntimeException("Rol con id " + id + " no encontrado"));
    }

    public void eliminar(Integer id) {
        rolRepository.deleteById(id);
    }
}