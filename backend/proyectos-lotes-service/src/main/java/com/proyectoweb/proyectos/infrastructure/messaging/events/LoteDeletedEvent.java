package com.proyectoweb.proyectos.infrastructure.messaging.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoteDeletedEvent {
    private UUID eventId;
    private LocalDateTime occurredOn;
    private UUID loteId;
    private String tenantId;
    private UUID proyectoId;
    private String numeroLote;

    public LoteDeletedEvent() {
    }

    public LoteDeletedEvent(UUID eventId, LocalDateTime occurredOn, UUID loteId, String tenantId, UUID proyectoId, String numeroLote) {
        this.eventId = eventId;
        this.occurredOn = occurredOn;
        this.loteId = loteId;
        this.tenantId = tenantId;
        this.proyectoId = proyectoId;
        this.numeroLote = numeroLote;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    public void setOccurredOn(LocalDateTime occurredOn) {
        this.occurredOn = occurredOn;
    }

    public UUID getLoteId() {
        return loteId;
    }

    public void setLoteId(UUID loteId) {
        this.loteId = loteId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(UUID proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }
}
