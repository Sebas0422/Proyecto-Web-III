package com.proyectoweb.reservations.infrastructure.persistence.jpa.repositories;

import com.proyectoweb.reservations.domain.value_objects.LeadStatus;
import com.proyectoweb.reservations.infrastructure.persistence.jpa.models.LeadJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LeadJpaRepository extends JpaRepository<LeadJpaModel, UUID> {
    List<LeadJpaModel> findByTenantId(UUID tenantId);
    List<LeadJpaModel> findByTenantIdAndStatus(UUID tenantId, LeadStatus status);
    List<LeadJpaModel> findByAssignedTo(UUID salesAgentId);
    List<LeadJpaModel> findByProjectId(UUID projectId);
}
