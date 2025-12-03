package com.proyectoweb.reservations.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadStatusChangedEvent {
    private String leadId;
    private String tenantId;
    private String status;
    private String projectId;
    private LocalDateTime changedAt;
}
