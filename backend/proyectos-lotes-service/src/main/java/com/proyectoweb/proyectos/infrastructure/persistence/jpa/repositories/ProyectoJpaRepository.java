package com.proyectoweb.proyectos.infrastructure.persistence.jpa.repositories;

import com.proyectoweb.proyectos.infrastructure.persistence.jpa.models.ProyectoJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProyectoJpaRepository extends JpaRepository<ProyectoJpaModel, UUID> {
    List<ProyectoJpaModel> findByTenantId(UUID tenantId);
    List<ProyectoJpaModel> findByTenantIdAndActivo(UUID tenantId, Boolean activo);
}
