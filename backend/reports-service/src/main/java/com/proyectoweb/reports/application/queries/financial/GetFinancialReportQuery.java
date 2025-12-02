package com.proyectoweb.reports.application.queries.financial;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.FinancialReportDto;

import java.time.LocalDateTime;

public class GetFinancialReportQuery implements Command<FinancialReportDto> {
    private final String tenantId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public GetFinancialReportQuery(String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
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
