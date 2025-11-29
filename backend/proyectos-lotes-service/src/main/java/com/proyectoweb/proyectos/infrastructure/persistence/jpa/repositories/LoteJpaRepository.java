package com.proyectoweb.proyectos.infrastructure.persistence.jpa.repositories;

import com.proyectoweb.proyectos.domain.value_objects.EstadoLote;
import com.proyectoweb.proyectos.infrastructure.persistence.jpa.models.LoteJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoteJpaRepository extends JpaRepository<LoteJpaModel, UUID> {
    List<LoteJpaModel> findByProyectoId(UUID proyectoId);
    List<LoteJpaModel> findByProyectoIdAndEstado(UUID proyectoId, EstadoLote estado);
    boolean existsByProyectoIdAndNumeroLote(UUID proyectoId, String numeroLote);
}
