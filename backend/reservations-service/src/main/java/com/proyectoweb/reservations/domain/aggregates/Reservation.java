package com.proyectoweb.reservations.domain.aggregates;

import com.proyectoweb.reservations.domain.events.ReservationCreated;
import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import com.proyectoweb.reservations.domain.value_objects.ReservationAmount;
import com.proyectoweb.reservations.domain.value_objects.ReservationStatus;
import com.proyectoweb.reservations.shared_kernel.AggregateRoot;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation extends AggregateRoot {
    private UUID tenantId;
    private UUID lotId;
    private UUID projectId;
    private CustomerInfo customerInfo;
    private ReservationAmount reservationAmount;
    private ReservationStatus status;
    private LocalDateTime reservationDate;
    private LocalDateTime expirationDate;
    private LocalDateTime confirmedAt;
    private LocalDateTime cancelledAt;
    private String notes;
    private UUID createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Reservation(UUID id) {
        super(id);
    }

    public static Reservation create(
            UUID id,
            UUID tenantId,
            UUID lotId,
            UUID projectId,
            CustomerInfo customerInfo,
            ReservationAmount reservationAmount,
            int expirationDays,
            UUID createdBy,
            String notes
    ) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }
        if (lotId == null) {
            throw new IllegalArgumentException("Lot ID cannot be null");
        }
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        if (expirationDays <= 0) {
            throw new IllegalArgumentException("Expiration days must be greater than zero");
        }

        Reservation reservation = new Reservation(id);
        reservation.tenantId = tenantId;
        reservation.lotId = lotId;
        reservation.projectId = projectId;
        reservation.customerInfo = customerInfo;
        reservation.reservationAmount = reservationAmount;
        reservation.status = ReservationStatus.PENDING;
        reservation.reservationDate = LocalDateTime.now();
        reservation.expirationDate = LocalDateTime.now().plusDays(expirationDays);
        reservation.notes = notes;
        reservation.createdBy = createdBy;
        reservation.createdAt = LocalDateTime.now();
        reservation.updatedAt = LocalDateTime.now();

        reservation.addDomainEvent(new ReservationCreated(
                id,
                tenantId,
                lotId,
                projectId,
                customerInfo,
                reservationAmount.amount(),
                LocalDateTime.now()
        ));

        return reservation;
    }

    public void confirm() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only PENDING reservations can be confirmed");
        }
        if (LocalDateTime.now().isAfter(expirationDate)) {
            throw new IllegalStateException("Cannot confirm expired reservation");
        }

        this.status = ReservationStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status == ReservationStatus.CANCELLED || status == ReservationStatus.EXPIRED) {
            throw new IllegalStateException("Reservation already cancelled or expired");
        }

        this.status = ReservationStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void expire() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only PENDING reservations can expire");
        }

        this.status = ReservationStatus.EXPIRED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate) && status == ReservationStatus.PENDING;
    }

    // Getters
    public UUID getTenantId() { return tenantId; }
    public UUID getLotId() { return lotId; }
    public UUID getProjectId() { return projectId; }
    public CustomerInfo getCustomerInfo() { return customerInfo; }
    public ReservationAmount getReservationAmount() { return reservationAmount; }
    public ReservationStatus getStatus() { return status; }
    public LocalDateTime getReservationDate() { return reservationDate; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public String getNotes() { return notes; }
    public UUID getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Reconstitute from persistence
    public static Reservation reconstitute(
            UUID id,
            UUID tenantId,
            UUID lotId,
            UUID projectId,
            CustomerInfo customerInfo,
            ReservationAmount reservationAmount,
            ReservationStatus status,
            LocalDateTime reservationDate,
            LocalDateTime expirationDate,
            LocalDateTime confirmedAt,
            LocalDateTime cancelledAt,
            String notes,
            UUID createdBy
    ) {
        Reservation reservation = new Reservation(id);
        reservation.tenantId = tenantId;
        reservation.lotId = lotId;
        reservation.projectId = projectId;
        reservation.customerInfo = customerInfo;
        reservation.reservationAmount = reservationAmount;
        reservation.status = status;
        reservation.reservationDate = reservationDate;
        reservation.expirationDate = expirationDate;
        reservation.confirmedAt = confirmedAt;
        reservation.cancelledAt = cancelledAt;
        reservation.notes = notes;
        reservation.createdBy = createdBy;
        return reservation;
    }
}
