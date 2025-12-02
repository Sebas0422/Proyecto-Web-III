package com.proyectoweb.reports.domain.repositories;

import com.proyectoweb.reports.domain.aggregates.MetricsSnapshot;

import java.util.Optional;
import java.util.UUID;

public interface MetricsSnapshotRepository {
    MetricsSnapshot save(MetricsSnapshot metrics);
    Optional<MetricsSnapshot> findByTenantId(UUID tenantId);
    MetricsSnapshot getOrCreateByTenantId(UUID tenantId);
}
