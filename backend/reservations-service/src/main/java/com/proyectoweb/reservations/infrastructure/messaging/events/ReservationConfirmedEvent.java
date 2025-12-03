package com.proyectoweb.reservations.infrastructure.messaging.events;

import java.time.LocalDateTime;

public class ReservationConfirmedEvent {
    private String reservationId;
    private String leadId;
    private String lotId;
    private String projectId;
    private String tenantId;
    private String paymentId;
    private LocalDateTime confirmedAt;

    public ReservationConfirmedEvent() {
    }

    public ReservationConfirmedEvent(String reservationId, String leadId, String lotId, String projectId, 
                                     String tenantId, String paymentId, LocalDateTime confirmedAt) {
        this.reservationId = reservationId;
        this.leadId = leadId;
        this.lotId = lotId;
        this.projectId = projectId;
        this.tenantId = tenantId;
        this.paymentId = paymentId;
        this.confirmedAt = confirmedAt;
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

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
