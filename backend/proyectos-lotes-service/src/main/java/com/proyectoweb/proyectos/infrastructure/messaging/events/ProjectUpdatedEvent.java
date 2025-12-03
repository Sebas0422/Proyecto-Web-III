package com.proyectoweb.proyectos.infrastructure.messaging.events;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectUpdatedEvent {
    private String projectId;
    private String tenantId;
    private String projectName;
    private String location;
    private BigDecimal totalArea;
    private Integer totalLots;
    private LocalDate startDate;
    private LocalDate estimatedEndDate;
    private String status;
    private LocalDateTime updatedAt;

    public ProjectUpdatedEvent() {
    }

    public ProjectUpdatedEvent(String projectId, String tenantId, String projectName, String location,
                              BigDecimal totalArea, Integer totalLots, LocalDate startDate,
                              LocalDate estimatedEndDate, String status, LocalDateTime updatedAt) {
        this.projectId = projectId;
        this.tenantId = tenantId;
        this.projectName = projectName;
        this.location = location;
        this.totalArea = totalArea;
        this.totalLots = totalLots;
        this.startDate = startDate;
        this.estimatedEndDate = estimatedEndDate;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(BigDecimal totalArea) {
        this.totalArea = totalArea;
    }

    public Integer getTotalLots() {
        return totalLots;
    }

    public void setTotalLots(Integer totalLots) {
        this.totalLots = totalLots;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEstimatedEndDate() {
        return estimatedEndDate;
    }

    public void setEstimatedEndDate(LocalDate estimatedEndDate) {
        this.estimatedEndDate = estimatedEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
