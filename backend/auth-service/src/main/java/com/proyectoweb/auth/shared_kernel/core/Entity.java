package com.proyectoweb.auth.shared_kernel.core;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class Entity {
    protected UUID id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected boolean isDeleted;
    private final List<DomainEvent> domainEvents;

    protected Entity() {
        this.domainEvents = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    protected Entity(UUID id) {
        this();
        this.id = id;
    }

    public void addDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    protected void checkRule(BusinessRule rule) throws BusinessRuleValidationException {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }

        if (!rule.isValid()) {
            throw new BusinessRuleValidationException(rule);
        }
    }

    protected void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.isDeleted = true;
        updateTimestamp();
    }

    public void restore() {
        this.isDeleted = false;
        updateTimestamp();
    }
}
