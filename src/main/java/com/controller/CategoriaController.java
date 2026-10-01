package com.controller;

import com.model.Categoria;
import com.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // GET http://localhost:8080/api/categorias
    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listarTodas();
    }

    // GET http://localhost:8080/api/categorias/3
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Integer id) {
        return categoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST http://localhost:8080/api/categorias  (body en JSON)
    @PostMapping
    public Categoria registrar(@RequestBody Categoria categoria) {
        return categoriaService.registrar(categoria);
    }

    // PUT http://localhost:8080/api/categorias/3
    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Integer id, @RequestBody Categoria categoria) {
        return categoriaService.actualizar(id, categoria);
    }

    // DELETE http://localhost:8080/api/categorias/3
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}