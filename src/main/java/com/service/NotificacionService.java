package com.service;

import com.model.Notificacion;
import com.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> listarTodas() {
        return notificacionRepository.findAll();
    }

    public List<Notificacion> listarNoLeidasPorUsuario(Integer idUsuario) {
        return notificacionRepository.findByUsuarioIdUsuarioAndLeidaFalse(idUsuario);
    }

    public Optional<Notificacion> buscarPorId(Integer id) {
        return notificacionRepository.findById(id);
    }

    public Notificacion registrar(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    public Notificacion actualizar(Integer id, Notificacion datosNuevos) {
        return notificacionRepository.findById(id).map(notiExistente -> {
            notiExistente.setLeida(datosNuevos.isLeida());
            return notificacionRepository.save(notiExistente);
        }).orElseThrow(() -> new RuntimeException("Notificación con id " + id + " no encontrada"));
    }

    public void eliminar(Integer id) {
        notificacionRepository.deleteById(id);
    }
}