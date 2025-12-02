package com.proyectoweb.reports.application.queries.leads;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.LeadsReportDto;

import java.time.LocalDateTime;

public class GetLeadsReportQuery implements Command<LeadsReportDto> {
    private final String tenantId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public GetLeadsReportQuery(String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        this.tenantId = tenantId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTenantId() {
        return tenantId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
