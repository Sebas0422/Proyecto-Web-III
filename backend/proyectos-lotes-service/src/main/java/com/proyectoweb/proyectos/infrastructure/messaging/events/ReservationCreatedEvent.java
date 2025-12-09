package com.proyectoweb.proyectos.infrastructure.messaging.events;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationCreatedEvent {
    private String reservationId;
    private String leadId;
    private String lotId;
    private String projectId;
    private String tenantId;
    private String lotUuid;
    private String projectUuid;
    private BigDecimal reservationAmount;
    private LocalDate reservationDate;
    private LocalDate expirationDate;
    private String status;
    private LocalDateTime createdAt;

    public ReservationCreatedEvent() {
    }

    public ReservationCreatedEvent(String reservationId, String leadId, String lotId, String projectId,
                                   String tenantId, String lotUuid, String projectUuid,
                                   BigDecimal reservationAmount, LocalDate reservationDate,
                                   LocalDate expirationDate, String status, LocalDateTime createdAt) {
        this.reservationId = reservationId;
        this.leadId = leadId;
        this.lotId = lotId;
        this.projectId = projectId;
        this.tenantId = tenantId;
        this.lotUuid = lotUuid;
        this.projectUuid = projectUuid;
        this.reservationAmount = reservationAmount;
        this.reservationDate = reservationDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
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

    public String getLotUuid() {
        return lotUuid;
    }

    public void setLotUuid(String lotUuid) {
        this.lotUuid = lotUuid;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public BigDecimal getReservationAmount() {
        return reservationAmount;
    }

    public void setReservationAmount(BigDecimal reservationAmount) {
        this.reservationAmount = reservationAmount;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
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
