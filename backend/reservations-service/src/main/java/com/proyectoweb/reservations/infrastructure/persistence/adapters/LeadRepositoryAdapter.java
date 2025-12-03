package com.proyectoweb.reservations.infrastructure.persistence.adapters;

import com.proyectoweb.reservations.domain.aggregates.Lead;
import com.proyectoweb.reservations.domain.repositories.LeadRepository;
import com.proyectoweb.reservations.domain.value_objects.LeadStatus;
import com.proyectoweb.reservations.infrastructure.persistence.jpa.models.LeadJpaModel;
import com.proyectoweb.reservations.infrastructure.persistence.jpa.repositories.LeadJpaRepository;
import com.proyectoweb.reservations.infrastructure.persistence.mappers.LeadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LeadRepositoryAdapter implements LeadRepository {

    private final LeadJpaRepository jpaRepository;
    private final LeadMapper mapper;

    @Override
    public Lead save(Lead lead) {
        LeadJpaModel jpaModel = mapper.toJpa(lead);
        LeadJpaModel saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Lead> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Lead> findByTenantId(UUID tenantId) {
        return jpaRepository.findByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lead> findByTenantIdAndStatus(UUID tenantId, LeadStatus status) {
        return jpaRepository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lead> findByAssignedTo(UUID salesAgentId) {
        return jpaRepository.findByAssignedTo(salesAgentId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lead> findByProjectId(UUID projectId) {
        return jpaRepository.findByProjectId(projectId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Lead lead) {
        jpaRepository.deleteById(lead.getId());
    }
}
