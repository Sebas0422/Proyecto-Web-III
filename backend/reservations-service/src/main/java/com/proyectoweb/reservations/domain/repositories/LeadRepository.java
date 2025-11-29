package com.proyectoweb.reservations.domain.repositories;

import com.proyectoweb.reservations.domain.aggregates.Lead;
import com.proyectoweb.reservations.domain.value_objects.LeadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeadRepository {
    Lead save(Lead lead);
    Optional<Lead> findById(UUID id);
    List<Lead> findByTenantId(UUID tenantId);
    List<Lead> findByTenantIdAndStatus(UUID tenantId, LeadStatus status);
    List<Lead> findByAssignedTo(UUID salesAgentId);
    List<Lead> findByProjectId(UUID projectId);
}
