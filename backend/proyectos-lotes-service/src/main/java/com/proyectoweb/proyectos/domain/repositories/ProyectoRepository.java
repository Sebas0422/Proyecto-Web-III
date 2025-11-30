package com.proyectoweb.proyectos.domain.repositories;

import com.proyectoweb.proyectos.domain.aggregates.Proyecto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProyectoRepository {
    Proyecto save(Proyecto proyecto);
    Optional<Proyecto> findById(UUID id);
    List<Proyecto> findByTenantId(UUID tenantId);
    List<Proyecto> findByTenantIdAndActivo(UUID tenantId, boolean activo);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
