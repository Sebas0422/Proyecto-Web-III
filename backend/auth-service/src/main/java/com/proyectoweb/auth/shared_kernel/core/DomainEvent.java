package com.proyectoweb.auth.shared_kernel.core;

import an.awesome.pipelinr.Notification;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public abstract class DomainEvent implements Serializable, Notification {
    private final LocalDateTime occurredOn;
    private final UUID eventId;
    private boolean consumed;

    protected DomainEvent() {
        this.occurredOn = LocalDateTime.now();
        this.eventId = UUID.randomUUID();
        this.consumed = false;
    }

    public void markAsConsumed() {
        this.consumed = true;
    }
}
