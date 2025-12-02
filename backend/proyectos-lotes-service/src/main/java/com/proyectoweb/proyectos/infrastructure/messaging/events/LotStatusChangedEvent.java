package com.proyectoweb.proyectos.infrastructure.messaging.events;

import java.time.LocalDateTime;

public class LotStatusChangedEvent {
    private Long lotId;
    private Long projectId;
    private Long tenantId;
    private String lotNumber;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changedAt;

    public LotStatusChangedEvent() {
    }

    public LotStatusChangedEvent(Long lotId, Long projectId, Long tenantId, String lotNumber, 
                                 String oldStatus, String newStatus, LocalDateTime changedAt) {
        this.lotId = lotId;
        this.projectId = projectId;
        this.tenantId = tenantId;
        this.lotNumber = lotNumber;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
