package com.proyectoweb.proyectos.infrastructure.messaging.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LotCreatedEvent {
    private Long lotId;
    private Long projectId;
    private Long tenantId;
    private String lotNumber;
    private BigDecimal area;
    private BigDecimal price;
    private String status;
    private LocalDateTime createdAt;

    public LotCreatedEvent() {
    }

    public LotCreatedEvent(Long lotId, Long projectId, Long tenantId, String lotNumber, 
                           BigDecimal area, BigDecimal price, String status, LocalDateTime createdAt) {
        this.lotId = lotId;
        this.projectId = projectId;
        this.tenantId = tenantId;
        this.lotNumber = lotNumber;
        this.area = area;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
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

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
