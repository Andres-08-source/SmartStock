package com.controller;

import com.model.Lote;
import com.service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lotes")
@CrossOrigin(origins = "*")
public class LoteController {

    @Autowired
    private LoteService loteService;

    @GetMapping
    public List<Lote> listar() {
        return loteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> buscarPorId(@PathVariable Integer id) {
        return loteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/por-vencer")
    public List<Lote> listarPorVencer(@RequestParam(defaultValue = "7") int dias) {
        return loteService.listarPorVencer(LocalDate.now().plusDays(dias));
    }

    @PostMapping
    public Lote registrar(@RequestBody Lote lote) {
        return loteService.registrar(lote);
    }

    @PutMapping("/{id}")
    public Lote actualizar(@PathVariable Integer id, @RequestBody Lote lote) {
        return loteService.actualizar(id, lote);
    }

}