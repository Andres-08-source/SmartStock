package com.controller;

import com.model.MovimientoInventario;
import com.service.MovimientoInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoService;

    @GetMapping
    public List<MovimientoInventario> listar() {
        return movimientoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoInventario> buscarPorId(@PathVariable Integer id) {
        return movimientoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/movimientos/producto/3 -> historial de movimientos de ese producto
    @GetMapping("/producto/{idProducto}")
    public List<MovimientoInventario> historialPorProducto(@PathVariable Integer idProducto) {
        return movimientoService.historialPorProducto(idProducto);
    }

    @PostMapping
    public MovimientoInventario registrar(@RequestBody MovimientoInventario movimiento) {
        return movimientoService.registrar(movimiento);
    }

}