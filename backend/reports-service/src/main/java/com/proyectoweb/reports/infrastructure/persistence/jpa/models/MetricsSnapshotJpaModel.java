package com.proyectoweb.reports.infrastructure.persistence.jpa.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "metrics_snapshot")
public class MetricsSnapshotJpaModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    
    @Column(name = "total_projects")
    private Integer totalProjects = 0;
    
    @Column(name = "active_projects")
    private Integer activeProjects = 0;
    
    @Column(name = "total_lots")
    private Integer totalLots = 0;
    
    @Column(name = "available_lots")
    private Integer availableLots = 0;
    
    @Column(name = "sold_lots")
    private Integer soldLots = 0;
    
    @Column(name = "reserved_lots")
    private Integer reservedLots = 0;
    
    @Column(name = "total_leads")
    private Integer totalLeads = 0;
    
    @Column(name = "active_leads")
    private Integer activeLeads = 0;
    
    @Column(name = "converted_leads")
    private Integer convertedLeads = 0;
    
    @Column(name = "total_reservations")
    private Integer totalReservations = 0;
    
    @Column(name = "confirmed_reservations")
    private Integer confirmedReservations = 0;
    
    @Column(name = "pending_reservations")
    private Integer pendingReservations = 0;
    
    @Column(name = "total_revenue", precision = 19, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;
    
    @Column(name = "pending_payments", precision = 19, scale = 2)
    private BigDecimal pendingPayments = BigDecimal.ZERO;
    
    @Column(name = "confirmed_payments", precision = 19, scale = 2)
    private BigDecimal confirmedPayments = BigDecimal.ZERO;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public MetricsSnapshotJpaModel() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    
    public Integer getTotalProjects() { return totalProjects; }
    public void setTotalProjects(Integer totalProjects) { this.totalProjects = totalProjects; }
    
    public Integer getActiveProjects() { return activeProjects; }
    public void setActiveProjects(Integer activeProjects) { this.activeProjects = activeProjects; }
    
    public Integer getTotalLots() { return totalLots; }
    public void setTotalLots(Integer totalLots) { this.totalLots = totalLots; }
    
    public Integer getAvailableLots() { return availableLots; }
    public void setAvailableLots(Integer availableLots) { this.availableLots = availableLots; }
    
    public Integer getSoldLots() { return soldLots; }
    public void setSoldLots(Integer soldLots) { this.soldLots = soldLots; }
    
    public Integer getReservedLots() { return reservedLots; }
    public void setReservedLots(Integer reservedLots) { this.reservedLots = reservedLots; }
    
    public Integer getTotalLeads() { return totalLeads; }
    public void setTotalLeads(Integer totalLeads) { this.totalLeads = totalLeads; }
    
    public Integer getActiveLeads() { return activeLeads; }
    public void setActiveLeads(Integer activeLeads) { this.activeLeads = activeLeads; }
    
    public Integer getConvertedLeads() { return convertedLeads; }
    public void setConvertedLeads(Integer convertedLeads) { this.convertedLeads = convertedLeads; }
    
    public Integer getTotalReservations() { return totalReservations; }
    public void setTotalReservations(Integer totalReservations) { this.totalReservations = totalReservations; }
    
    public Integer getConfirmedReservations() { return confirmedReservations; }
    public void setConfirmedReservations(Integer confirmedReservations) { this.confirmedReservations = confirmedReservations; }
    
    public Integer getPendingReservations() { return pendingReservations; }
    public void setPendingReservations(Integer pendingReservations) { this.pendingReservations = pendingReservations; }
    
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public BigDecimal getPendingPayments() { return pendingPayments; }
    public void setPendingPayments(BigDecimal pendingPayments) { this.pendingPayments = pendingPayments; }
    
    public BigDecimal getConfirmedPayments() { return confirmedPayments; }
    public void setConfirmedPayments(BigDecimal confirmedPayments) { this.confirmedPayments = confirmedPayments; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
