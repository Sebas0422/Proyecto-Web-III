package com.proyectoweb.reports.domain.aggregates;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MetricsSnapshot {
    private Long id;
    private UUID tenantId;
    
    private Integer totalProjects;
    private Integer activeProjects;
    private Integer totalLots;
    private Integer availableLots;
    private Integer soldLots;
    private Integer reservedLots;
    
    private Integer totalLeads;
    private Integer activeLeads;
    private Integer convertedLeads;
    
    private Integer totalReservations;
    private Integer confirmedReservations;
    private Integer pendingReservations;
    
    private BigDecimal totalRevenue;
    private BigDecimal pendingPayments;
    private BigDecimal confirmedPayments;
    
    private LocalDateTime updatedAt;

    public MetricsSnapshot(UUID tenantId) {
        this.tenantId = tenantId;
        this.totalProjects = 0;
        this.activeProjects = 0;
        this.totalLots = 0;
        this.availableLots = 0;
        this.soldLots = 0;
        this.reservedLots = 0;
        this.totalLeads = 0;
        this.activeLeads = 0;
        this.convertedLeads = 0;
        this.totalReservations = 0;
        this.confirmedReservations = 0;
        this.pendingReservations = 0;
        this.totalRevenue = BigDecimal.ZERO;
        this.pendingPayments = BigDecimal.ZERO;
        this.confirmedPayments = BigDecimal.ZERO;
        this.updatedAt = LocalDateTime.now();
    }

    public MetricsSnapshot(Long id, UUID tenantId, Integer totalProjects, Integer activeProjects,
                          Integer totalLots, Integer availableLots, Integer soldLots, Integer reservedLots,
                          Integer totalLeads, Integer activeLeads, Integer convertedLeads,
                          Integer totalReservations, Integer confirmedReservations, Integer pendingReservations,
                          BigDecimal totalRevenue, BigDecimal pendingPayments, BigDecimal confirmedPayments,
                          LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.totalProjects = totalProjects;
        this.activeProjects = activeProjects;
        this.totalLots = totalLots;
        this.availableLots = availableLots;
        this.soldLots = soldLots;
        this.reservedLots = reservedLots;
        this.totalLeads = totalLeads;
        this.activeLeads = activeLeads;
        this.convertedLeads = convertedLeads;
        this.totalReservations = totalReservations;
        this.confirmedReservations = confirmedReservations;
        this.pendingReservations = pendingReservations;
        this.totalRevenue = totalRevenue;
        this.pendingPayments = pendingPayments;
        this.confirmedPayments = confirmedPayments;
        this.updatedAt = updatedAt;
    }

    public void incrementProjectCount(boolean isActive) {
        this.totalProjects++;
        if (isActive) {
            this.activeProjects++;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementLotCount(String estado) {
        this.totalLots++;
        switch (estado.toUpperCase()) {
            case "DISPONIBLE" -> this.availableLots++;
            case "VENDIDO" -> this.soldLots++;
            case "RESERVADO" -> this.reservedLots++;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLotStatus(String oldStatus, String newStatus) {
        decrementLotByStatus(oldStatus);
        incrementLotByStatus(newStatus);
        this.updatedAt = LocalDateTime.now();
    }

    private void decrementLotByStatus(String status) {
        switch (status.toUpperCase()) {
            case "DISPONIBLE" -> this.availableLots = Math.max(0, this.availableLots - 1);
            case "VENDIDO" -> this.soldLots = Math.max(0, this.soldLots - 1);
            case "RESERVADO" -> this.reservedLots = Math.max(0, this.reservedLots - 1);
        }
    }

    private void incrementLotByStatus(String status) {
        switch (status.toUpperCase()) {
            case "DISPONIBLE" -> this.availableLots++;
            case "VENDIDO" -> this.soldLots++;
            case "RESERVADO" -> this.reservedLots++;
        }
    }

    public void incrementLeadCount(boolean isActive) {
        this.totalLeads++;
        if (isActive) {
            this.activeLeads++;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void markLeadAsConverted() {
        this.convertedLeads++;
        this.activeLeads = Math.max(0, this.activeLeads - 1);
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementReservation(boolean isConfirmed) {
        this.totalReservations++;
        if (isConfirmed) {
            this.confirmedReservations++;
        } else {
            this.pendingReservations++;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void confirmReservation() {
        this.confirmedReservations++;
        this.pendingReservations = Math.max(0, this.pendingReservations - 1);
        this.updatedAt = LocalDateTime.now();
    }

    public void addPayment(BigDecimal amount, boolean isConfirmed) {
        this.totalRevenue = this.totalRevenue.add(amount);
        if (isConfirmed) {
            this.confirmedPayments = this.confirmedPayments.add(amount);
        } else {
            this.pendingPayments = this.pendingPayments.add(amount);
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void confirmPayment(BigDecimal amount) {
        this.confirmedPayments = this.confirmedPayments.add(amount);
        this.pendingPayments = this.pendingPayments.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public Integer getTotalProjects() { return totalProjects; }
    public Integer getActiveProjects() { return activeProjects; }
    public Integer getTotalLots() { return totalLots; }
    public Integer getAvailableLots() { return availableLots; }
    public Integer getSoldLots() { return soldLots; }
    public Integer getReservedLots() { return reservedLots; }
    public Integer getTotalLeads() { return totalLeads; }
    public Integer getActiveLeads() { return activeLeads; }
    public Integer getConvertedLeads() { return convertedLeads; }
    public Integer getTotalReservations() { return totalReservations; }
    public Integer getConfirmedReservations() { return confirmedReservations; }
    public Integer getPendingReservations() { return pendingReservations; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public BigDecimal getPendingPayments() { return pendingPayments; }
    public BigDecimal getConfirmedPayments() { return confirmedPayments; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
