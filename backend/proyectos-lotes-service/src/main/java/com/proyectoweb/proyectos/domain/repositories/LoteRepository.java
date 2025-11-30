package com.proyectoweb.proyectos.domain.repositories;

import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.value_objects.EstadoLote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoteRepository {
    Lote save(Lote lote);
    Optional<Lote> findById(UUID id);
    List<Lote> findByProyectoId(UUID proyectoId);
    List<Lote> findByProyectoIdAndEstado(UUID proyectoId, EstadoLote estado);
    boolean existsByProyectoIdAndNumeroLote(UUID proyectoId, String numeroLote);
    void deleteById(UUID id);
}
