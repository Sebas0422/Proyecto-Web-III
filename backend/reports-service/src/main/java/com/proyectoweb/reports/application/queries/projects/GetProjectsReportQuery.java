package com.proyectoweb.reports.application.queries.projects;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.ProjectsReportDto;

import java.time.LocalDateTime;

public class GetProjectsReportQuery implements Command<ProjectsReportDto> {
    private final String tenantId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public GetProjectsReportQuery(String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
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
