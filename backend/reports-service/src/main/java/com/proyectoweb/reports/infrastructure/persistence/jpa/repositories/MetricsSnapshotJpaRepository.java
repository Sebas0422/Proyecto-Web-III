package com.proyectoweb.reports.infrastructure.persistence.jpa.repositories;

import com.proyectoweb.reports.infrastructure.persistence.jpa.models.MetricsSnapshotJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetricsSnapshotJpaRepository extends JpaRepository<MetricsSnapshotJpaModel, Long> {
    Optional<MetricsSnapshotJpaModel> findByTenantId(UUID tenantId);
}
