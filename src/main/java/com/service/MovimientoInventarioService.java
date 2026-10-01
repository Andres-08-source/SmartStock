// Ubicación: src/main/java/com/service/MovimientoInventarioService.java
package com.service;

import com.model.Inventario;
import com.model.MovimientoInventario;
import com.repository.InventarioRepository;
import com.repository.MovimientoInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientoInventarioService {

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<MovimientoInventario> listarTodos() {
        return movimientoRepository.findAll();
    }

    public Optional<MovimientoInventario> buscarPorId(Integer id) {
        return movimientoRepository.findById(id);
    }

    public List<MovimientoInventario> historialPorProducto(Integer idProducto) {
        return movimientoRepository.findByProductoIdProductoOrderByFechaMovimientoDesc(idProducto);
    }


    @Transactional
    public MovimientoInventario registrar(MovimientoInventario movimiento) {
        Integer idProducto = movimiento.getProducto().getIdProducto();


        Inventario inventario = inventarioRepository.findByProductoIdProducto(idProducto)
                .orElseThrow(() -> new RuntimeException("Este producto no tiene inventario registrado"));

        int cantidadActual = inventario.getCantidadActual();
        int nuevaCantidad;

        switch (movimiento.getTipoMovimiento()) {
            case "entrada" -> nuevaCantidad = cantidadActual + movimiento.getCantidad();
            case "salida" -> nuevaCantidad = cantidadActual - movimiento.getCantidad();
            case "ajuste" -> nuevaCantidad = movimiento.getCantidad(); // el ajuste fija la cantidad directamente
            default -> throw new RuntimeException("Tipo de movimiento inválido: " + movimiento.getTipoMovimiento());
        }

        if (nuevaCantidad < 0) {
            throw new RuntimeException("Stock insuficiente para registrar esta salida");
        }

        inventario.setCantidadActual(nuevaCantidad);
        inventarioRepository.save(inventario);

        return movimientoRepository.save(movimiento);
    }
}