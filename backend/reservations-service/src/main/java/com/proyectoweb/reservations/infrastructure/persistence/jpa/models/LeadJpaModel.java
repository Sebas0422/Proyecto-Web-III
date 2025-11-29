package com.proyectoweb.reservations.infrastructure.persistence.jpa.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "leads", indexes = {
        @Index(name = "idx_tenant_id", columnList = "tenant_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_assigned_to", columnList = "assigned_to")
})
@Getter
@Setter
public class LeadJpaModel {
    
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @Column(name = "project_id", columnDefinition = "BINARY(16)")
    private UUID projectId;

    @Column(name = "lot_id", columnDefinition = "BINARY(16)")
    private UUID lotId;

    @Column(name = "customer_name", nullable = false, length = 200)
    private String customerName;

    @Column(name = "customer_email", length = 150)
    private String customerEmail;

    @Column(name = "customer_phone", nullable = false, length = 50)
    private String customerPhone;

    @Column(name = "customer_document", length = 50)
    private String customerDocument;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private com.proyectoweb.reservations.domain.value_objects.LeadStatus status;

    @Column(length = 100)
    private String source;

    @Column(name = "interest_level", length = 20)
    private String interestLevel;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "assigned_to", columnDefinition = "BINARY(16)")
    private UUID assignedTo;

    @Column(name = "contacted_at")
    private LocalDateTime contactedAt;

    @Column(name = "converted_at")
    private LocalDateTime convertedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
