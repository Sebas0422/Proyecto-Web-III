package com.proyectoweb.reports.domain.events;

import an.awesome.pipelinr.Notification;

import java.time.LocalDateTime;

public class ReportExported implements Notification {
    private final String reportId;
    private final String exportFormat;
    private final String fileName;
    private final LocalDateTime exportedAt;

    public ReportExported(String reportId, String exportFormat, String fileName, LocalDateTime exportedAt) {
        this.reportId = reportId;
        this.exportFormat = exportFormat;
        this.fileName = fileName;
        this.exportedAt = exportedAt;
    }

    public String getReportId() {
        return reportId;
    }

    public String getExportFormat() {
        return exportFormat;
    }

    public String getFileName() {
        return fileName;
    }

    public LocalDateTime getExportedAt() {
        return exportedAt;
    }
}
