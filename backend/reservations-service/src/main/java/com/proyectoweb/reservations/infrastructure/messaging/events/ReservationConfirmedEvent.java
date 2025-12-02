package com.proyectoweb.reservations.infrastructure.messaging.events;

import java.time.LocalDateTime;

public class ReservationConfirmedEvent {
    private Long reservationId;
    private Long leadId;
    private Long lotId;
    private Long projectId;
    private Long tenantId;
    private Long paymentId;
    private LocalDateTime confirmedAt;

    public ReservationConfirmedEvent() {
    }

    public ReservationConfirmedEvent(Long reservationId, Long leadId, Long lotId, Long projectId, 
                                     Long tenantId, Long paymentId, LocalDateTime confirmedAt) {
        this.reservationId = reservationId;
        this.leadId = leadId;
        this.lotId = lotId;
        this.projectId = projectId;
        this.tenantId = tenantId;
        this.paymentId = paymentId;
        this.confirmedAt = confirmedAt;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
