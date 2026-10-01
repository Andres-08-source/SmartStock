package com.controller;

import com.model.Alerta;
import com.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "*")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @GetMapping
    public List<Alerta> listar() {
        return alertaService.listarTodas();
    }

    @GetMapping("/pendientes")
    public List<Alerta> listarPendientes() {
        return alertaService.listarPendientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alerta> buscarPorId(@PathVariable Integer id) {
        return alertaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Alerta registrar(@RequestBody Alerta alerta) {
        return alertaService.registrar(alerta);
    }

    @PutMapping("/{id}")
    public Alerta actualizar(@PathVariable Integer id, @RequestBody Alerta alerta) {
        return alertaService.actualizar(id, alerta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        alertaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}