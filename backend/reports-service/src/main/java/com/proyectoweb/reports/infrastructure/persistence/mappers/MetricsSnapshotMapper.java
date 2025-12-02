package com.proyectoweb.reports.infrastructure.persistence.mappers;

import com.proyectoweb.reports.domain.aggregates.MetricsSnapshot;
import com.proyectoweb.reports.infrastructure.persistence.jpa.models.MetricsSnapshotJpaModel;
import org.springframework.stereotype.Component;

@Component
public class MetricsSnapshotMapper {

    public MetricsSnapshot toDomain(MetricsSnapshotJpaModel jpaModel) {
        return new MetricsSnapshot(
                jpaModel.getId(),
                jpaModel.getTenantId(),
                jpaModel.getTotalProjects(),
                jpaModel.getActiveProjects(),
                jpaModel.getTotalLots(),
                jpaModel.getAvailableLots(),
                jpaModel.getSoldLots(),
                jpaModel.getReservedLots(),
                jpaModel.getTotalLeads(),
                jpaModel.getActiveLeads(),
                jpaModel.getConvertedLeads(),
                jpaModel.getTotalReservations(),
                jpaModel.getConfirmedReservations(),
                jpaModel.getPendingReservations(),
                jpaModel.getTotalRevenue(),
                jpaModel.getPendingPayments(),
                jpaModel.getConfirmedPayments(),
                jpaModel.getUpdatedAt()
        );
    }

    public MetricsSnapshotJpaModel toJpa(MetricsSnapshot domain) {
        MetricsSnapshotJpaModel jpaModel = new MetricsSnapshotJpaModel();
        jpaModel.setId(domain.getId());
        jpaModel.setTenantId(domain.getTenantId());
        jpaModel.setTotalProjects(domain.getTotalProjects());
        jpaModel.setActiveProjects(domain.getActiveProjects());
        jpaModel.setTotalLots(domain.getTotalLots());
        jpaModel.setAvailableLots(domain.getAvailableLots());
        jpaModel.setSoldLots(domain.getSoldLots());
        jpaModel.setReservedLots(domain.getReservedLots());
        jpaModel.setTotalLeads(domain.getTotalLeads());
        jpaModel.setActiveLeads(domain.getActiveLeads());
        jpaModel.setConvertedLeads(domain.getConvertedLeads());
        jpaModel.setTotalReservations(domain.getTotalReservations());
        jpaModel.setConfirmedReservations(domain.getConfirmedReservations());
        jpaModel.setPendingReservations(domain.getPendingReservations());
        jpaModel.setTotalRevenue(domain.getTotalRevenue());
        jpaModel.setPendingPayments(domain.getPendingPayments());
        jpaModel.setConfirmedPayments(domain.getConfirmedPayments());
        jpaModel.setUpdatedAt(domain.getUpdatedAt());
        return jpaModel;
    }
}
