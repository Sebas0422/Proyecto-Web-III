package com.proyectoweb.reports.application.queries.sales;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.SalesReportDto;

import java.time.LocalDateTime;

public class GetSalesReportQuery implements Command<SalesReportDto> {
    private final String tenantId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public GetSalesReportQuery(String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
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
