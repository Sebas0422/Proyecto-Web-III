package com.proyectoweb.proyectos.domain.repositories;

import com.proyectoweb.proyectos.domain.aggregates.LoteAttachment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoteAttachmentRepository {
    LoteAttachment save(LoteAttachment attachment);
    Optional<LoteAttachment> findById(UUID id);
    List<LoteAttachment> findByLoteId(UUID loteId);
    void deleteById(UUID id);
}
