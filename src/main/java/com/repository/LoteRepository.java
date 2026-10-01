package com.repository;

import com.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    // Útil para el sistema de alertas: lotes que vencen antes de cierta fecha
    List<Lote> findByFechaVencimientoBefore(LocalDate fecha);
}