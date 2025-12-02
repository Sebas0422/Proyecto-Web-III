package com.proyectoweb.reports.infrastructure.persistence.adapters;

import com.proyectoweb.reports.domain.aggregates.MetricsSnapshot;
import com.proyectoweb.reports.domain.repositories.MetricsSnapshotRepository;
import com.proyectoweb.reports.infrastructure.persistence.jpa.repositories.MetricsSnapshotJpaRepository;
import com.proyectoweb.reports.infrastructure.persistence.mappers.MetricsSnapshotMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class MetricsSnapshotRepositoryAdapter implements MetricsSnapshotRepository {

    private final MetricsSnapshotJpaRepository jpaRepository;
    private final MetricsSnapshotMapper mapper;

    public MetricsSnapshotRepositoryAdapter(MetricsSnapshotJpaRepository jpaRepository, MetricsSnapshotMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public MetricsSnapshot save(MetricsSnapshot metrics) {
        var jpaModel = mapper.toJpa(metrics);
        var saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<MetricsSnapshot> findByTenantId(UUID tenantId) {
        return jpaRepository.findByTenantId(tenantId)
                .map(mapper::toDomain);
    }

    @Override
    public MetricsSnapshot getOrCreateByTenantId(UUID tenantId) {
        return findByTenantId(tenantId)
                .orElseGet(() -> {
                    MetricsSnapshot newMetrics = new MetricsSnapshot(tenantId);
                    return save(newMetrics);
                });
    }
}
