package com.controller;

import com.model.Inventario;
import com.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<Inventario> listar() {
        return inventarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarPorId(@PathVariable Integer id) {
        return inventarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Inventario registrar(@RequestBody Inventario inventario) {
        return inventarioService.registrar(inventario);
    }

    @PutMapping("/{id}")
    public Inventario actualizar(@PathVariable Integer id, @RequestBody Inventario inventario) {
        return inventarioService.actualizar(id, inventario);
    }
}