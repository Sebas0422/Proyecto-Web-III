package com.proyectoweb.reports.domain.events;

import an.awesome.pipelinr.Notification;

import java.time.LocalDateTime;

public class ReportGenerated implements Notification {
    private final String reportId;
    private final String reportType;
    private final String tenantId;
    private final String generatedBy;
    private final LocalDateTime generatedAt;

    public ReportGenerated(String reportId, String reportType, String tenantId, String generatedBy, LocalDateTime generatedAt) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.tenantId = tenantId;
        this.generatedBy = generatedBy;
        this.generatedAt = generatedAt;
    }

    public String getReportId() {
        return reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}
