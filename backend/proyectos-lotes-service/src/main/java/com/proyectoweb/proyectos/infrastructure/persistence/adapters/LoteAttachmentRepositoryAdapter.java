package com.proyectoweb.proyectos.infrastructure.persistence.adapters;

import com.proyectoweb.proyectos.domain.aggregates.LoteAttachment;
import com.proyectoweb.proyectos.domain.repositories.LoteAttachmentRepository;
import com.proyectoweb.proyectos.infrastructure.persistence.jpa.models.LoteAttachmentJpaModel;
import com.proyectoweb.proyectos.infrastructure.persistence.jpa.repositories.LoteAttachmentJpaRepository;
import com.proyectoweb.proyectos.infrastructure.persistence.mappers.LoteAttachmentMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class LoteAttachmentRepositoryAdapter implements LoteAttachmentRepository {

    private final LoteAttachmentJpaRepository jpaRepository;
    private final LoteAttachmentMapper mapper;

    public LoteAttachmentRepositoryAdapter(LoteAttachmentJpaRepository jpaRepository, LoteAttachmentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public LoteAttachment save(LoteAttachment attachment) {
        LoteAttachmentJpaModel jpa = mapper.toJpa(attachment);
        LoteAttachmentJpaModel saved = jpaRepository.save(jpa);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<LoteAttachment> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<LoteAttachment> findByLoteId(UUID loteId) {
        return jpaRepository.findByLoteId(loteId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
