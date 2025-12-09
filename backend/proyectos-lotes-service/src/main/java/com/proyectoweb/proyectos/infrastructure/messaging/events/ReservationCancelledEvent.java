package com.proyectoweb.proyectos.infrastructure.messaging.events;

import java.time.LocalDateTime;

public class ReservationCancelledEvent {
    private String reservationId;
    private String lotUuid;
    private String projectUuid;
    private String tenantId;
    private LocalDateTime cancelledAt;

    public ReservationCancelledEvent() {
    }

    public ReservationCancelledEvent(String reservationId, String lotUuid, String projectUuid,
                                     String tenantId, LocalDateTime cancelledAt) {
        this.reservationId = reservationId;
        this.lotUuid = lotUuid;
        this.projectUuid = projectUuid;
        this.tenantId = tenantId;
        this.cancelledAt = cancelledAt;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
