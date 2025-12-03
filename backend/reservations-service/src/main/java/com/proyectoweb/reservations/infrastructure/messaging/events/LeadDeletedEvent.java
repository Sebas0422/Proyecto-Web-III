package com.proyectoweb.reservations.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadDeletedEvent {
    private String leadId;
    private String tenantId;
    private String customerName;
    private LocalDateTime deletedAt;
}
