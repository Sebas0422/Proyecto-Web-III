package com.proyectoweb.reports.domain.aggregates;

import com.proyectoweb.reports.domain.value_objects.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReportExecution {
    private String id;
    private String tenantId;
    private ReportType reportType;
    private DateRange dateRange;
    private ReportStatus status;
    private Map<String, Object> parameters;
    private Map<String, Object> data;
    private String generatedBy;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
    private String errorMessage;

    // Constructor privado para forzar uso de factory methods
    private ReportExecution() {
    }

    public static ReportExecution create(
            String tenantId,
            ReportType reportType,
            DateRange dateRange,
            Map<String, Object> parameters,
            String generatedBy
    ) {
        ReportExecution execution = new ReportExecution();
        execution.id = UUID.randomUUID().toString();
        execution.tenantId = tenantId;
        execution.reportType = reportType;
        execution.dateRange = dateRange;
        execution.status = ReportStatus.PENDING;
        execution.parameters = parameters;
        execution.generatedBy = generatedBy;
        execution.requestedAt = LocalDateTime.now();
        return execution;
    }

    public static ReportExecution reconstitute(
            String id,
            String tenantId,
            ReportType reportType,
            DateRange dateRange,
            ReportStatus status,
            Map<String, Object> parameters,
            Map<String, Object> data,
            String generatedBy,
            LocalDateTime requestedAt,
            LocalDateTime completedAt,
            String errorMessage
    ) {
        ReportExecution execution = new ReportExecution();
        execution.id = id;
        execution.tenantId = tenantId;
        execution.reportType = reportType;
        execution.dateRange = dateRange;
        execution.status = status;
        execution.parameters = parameters;
        execution.data = data;
        execution.generatedBy = generatedBy;
        execution.requestedAt = requestedAt;
        execution.completedAt = completedAt;
        execution.errorMessage = errorMessage;
        return execution;
    }

    public void startProcessing() {
        if (!this.status.equals(ReportStatus.PENDING)) {
            throw new IllegalStateException("Can only start processing a pending report");
        }
        this.status = ReportStatus.PROCESSING;
    }

    public void complete(Map<String, Object> data) {
        if (!this.status.equals(ReportStatus.PROCESSING)) {
            throw new IllegalStateException("Can only complete a processing report");
        }
        this.data = data;
        this.status = ReportStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void fail(String errorMessage) {
        this.status = ReportStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
    }

    // Getters manuales
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
