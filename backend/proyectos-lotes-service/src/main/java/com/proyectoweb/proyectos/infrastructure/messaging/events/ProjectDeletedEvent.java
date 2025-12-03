package com.proyectoweb.proyectos.infrastructure.messaging.events;

import java.time.LocalDateTime;

public class ProjectDeletedEvent {
    private String projectId;
    private String tenantId;
    private String projectName;
    private LocalDateTime deletedAt;

    public ProjectDeletedEvent() {
    }

    public ProjectDeletedEvent(String projectId, String tenantId, String projectName, LocalDateTime deletedAt) {
        this.projectId = projectId;
        this.tenantId = tenantId;
        this.projectName = projectName;
        this.deletedAt = deletedAt;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
