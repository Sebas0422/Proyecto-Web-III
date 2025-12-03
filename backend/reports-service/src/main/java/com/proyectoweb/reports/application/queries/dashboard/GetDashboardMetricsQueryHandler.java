package com.proyectoweb.reports.application.queries.dashboard;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.DashboardMetricsDto;
import com.proyectoweb.reports.domain.aggregates.MetricsSnapshot;
import com.proyectoweb.reports.domain.repositories.MetricsSnapshotRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetDashboardMetricsQueryHandler implements Command.Handler<GetDashboardMetricsQuery, DashboardMetricsDto> {

    private final MetricsSnapshotRepository metricsRepository;

    public GetDashboardMetricsQueryHandler(MetricsSnapshotRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    @Override
    public DashboardMetricsDto handle(GetDashboardMetricsQuery query) {
        UUID tenantId = UUID.fromString(query.getTenantId());
        
        MetricsSnapshot metrics = metricsRepository.getOrCreateByTenantId(tenantId);
        
        double conversionRate = 0.0;
        if (metrics.getTotalLeads() > 0) {
            conversionRate = (metrics.getConvertedLeads() * 100.0) / metrics.getTotalLeads();
        }
        
        return new DashboardMetricsDto(
                metrics.getTotalProjects(),
                metrics.getActiveProjects(),
                metrics.getTotalLots(),
                metrics.getAvailableLots(),
                metrics.getSoldLots(),
                metrics.getReservedLots(),
                metrics.getTotalLeads(),
                metrics.getActiveLeads(),
                metrics.getConvertedLeads(),
                conversionRate,
                metrics.getTotalReservations(),
                metrics.getConfirmedReservations(),
                metrics.getPendingReservations(),
                metrics.getTotalRevenue(),
                metrics.getPendingPayments(),
                metrics.getConfirmedPayments(),
                "BOB"
        );
    }
}
