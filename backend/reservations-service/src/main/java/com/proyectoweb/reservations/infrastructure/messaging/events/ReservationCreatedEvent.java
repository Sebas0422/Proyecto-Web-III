package com.proyectoweb.reservations.infrastructure.messaging.events;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationCreatedEvent {
    private Long reservationId;
    private Long leadId;
    private Long lotId;
    private Long projectId;
    private String tenantId;
    private BigDecimal reservationAmount;
    private LocalDate reservationDate;
    private LocalDate expirationDate;
    private String status;
    private LocalDateTime createdAt;

    public ReservationCreatedEvent() {
    }

    public ReservationCreatedEvent(Long reservationId, Long leadId, Long lotId, Long projectId, 
                                   String tenantId, BigDecimal reservationAmount, LocalDate reservationDate, 
                                   LocalDate expirationDate, String status, LocalDateTime createdAt) {
        this.reservationId = reservationId;
        this.leadId = leadId;
        this.lotId = lotId;
        this.projectId = projectId;
        this.tenantId = tenantId;
        this.reservationAmount = reservationAmount;
        this.reservationDate = reservationDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getLeadId() {
        return leadId;
    }

    public void setLeadId(Long leadId) {
        this.leadId = leadId;
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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
