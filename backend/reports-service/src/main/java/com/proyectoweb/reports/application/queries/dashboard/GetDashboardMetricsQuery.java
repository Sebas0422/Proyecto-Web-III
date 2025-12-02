package com.proyectoweb.reports.application.queries.dashboard;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.DashboardMetricsDto;

public class GetDashboardMetricsQuery implements Command<DashboardMetricsDto> {
    private final String tenantId;

    public GetDashboardMetricsQuery(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}
