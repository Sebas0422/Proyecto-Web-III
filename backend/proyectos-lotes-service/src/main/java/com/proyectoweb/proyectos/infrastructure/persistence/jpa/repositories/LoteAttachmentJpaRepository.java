package com.proyectoweb.proyectos.infrastructure.persistence.jpa.repositories;

import com.proyectoweb.proyectos.infrastructure.persistence.jpa.models.LoteAttachmentJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoteAttachmentJpaRepository extends JpaRepository<LoteAttachmentJpaModel, UUID> {
    List<LoteAttachmentJpaModel> findByLoteId(UUID loteId);
}
