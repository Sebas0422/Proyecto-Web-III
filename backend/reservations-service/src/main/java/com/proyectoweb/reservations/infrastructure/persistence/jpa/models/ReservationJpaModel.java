package com.proyectoweb.reservations.infrastructure.persistence.jpa.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "idx_tenant_id", columnList = "tenant_id"),
        @Index(name = "idx_lot_id", columnList = "lot_id"),
        @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
public class ReservationJpaModel {
    
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @Column(name = "lot_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID lotId;

    @Column(name = "project_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID projectId;

    @Column(name = "customer_name", nullable = false, length = 200)
    private String customerName;

    @Column(name = "customer_email", length = 150)
    private String customerEmail;

    @Column(name = "customer_phone", nullable = false, length = 50)
    private String customerPhone;

    @Column(name = "customer_document", length = 50)
    private String customerDocument;

    @Column(name = "reservation_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal reservationAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private com.proyectoweb.reservations.domain.value_objects.ReservationStatus status;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by", columnDefinition = "BINARY(16)")
    private UUID createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
