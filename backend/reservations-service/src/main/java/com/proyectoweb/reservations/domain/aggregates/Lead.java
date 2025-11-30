package com.proyectoweb.reservations.domain.aggregates;

import com.proyectoweb.reservations.domain.events.LeadCreated;
import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import com.proyectoweb.reservations.domain.value_objects.LeadStatus;
import com.proyectoweb.reservations.shared_kernel.AggregateRoot;

import java.time.LocalDateTime;
import java.util.UUID;

public class Lead extends AggregateRoot {
    private UUID tenantId;
    private UUID projectId;
    private UUID lotId;
    private CustomerInfo customerInfo;
    private LeadStatus status;
    private String source;
    private String interestLevel;
    private String notes;
    private UUID assignedTo;
    private LocalDateTime contactedAt;
    private LocalDateTime convertedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Lead(UUID id) {
        super(id);
    }

    public static Lead create(
            UUID id,
            UUID tenantId,
            CustomerInfo customerInfo,
            String source,
            String interestLevel,
            String notes,
            UUID projectId,
            UUID lotId
    ) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        Lead lead = new Lead(id);
        lead.tenantId = tenantId;
        lead.customerInfo = customerInfo;
        lead.status = LeadStatus.NEW;
        lead.source = source;
        lead.interestLevel = interestLevel;
        lead.notes = notes;
        lead.projectId = projectId;
        lead.lotId = lotId;
        lead.createdAt = LocalDateTime.now();
        lead.updatedAt = LocalDateTime.now();

        lead.addDomainEvent(new LeadCreated(
                id,
                tenantId,
                customerInfo,
                source,
                LocalDateTime.now()
        ));

        return lead;
    }

    public void assignTo(UUID salesAgentId) {
        if (salesAgentId == null) {
            throw new IllegalArgumentException("Sales agent ID cannot be null");
        }
        this.assignedTo = salesAgentId;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(LeadStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();

        if (newStatus == LeadStatus.CONTACTED && this.contactedAt == null) {
            this.contactedAt = LocalDateTime.now();
        }
        if (newStatus == LeadStatus.CONVERTED) {
            this.convertedAt = LocalDateTime.now();
        }
    }

    public void addNote(String note) {
        if (this.notes == null) {
            this.notes = note;
        } else {
            this.notes = this.notes + "\n" + LocalDateTime.now() + ": " + note;
        }
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getTenantId() { return tenantId; }
    public UUID getProjectId() { return projectId; }
    public UUID getLotId() { return lotId; }
    public CustomerInfo getCustomerInfo() { return customerInfo; }
    public LeadStatus getStatus() { return status; }
    public String getSource() { return source; }
    public String getInterestLevel() { return interestLevel; }
    public String getNotes() { return notes; }
    public UUID getAssignedTo() { return assignedTo; }
    public LocalDateTime getContactedAt() { return contactedAt; }
    public LocalDateTime getConvertedAt() { return convertedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Reconstitute from persistence
    public static Lead reconstitute(
            UUID id,
            UUID tenantId,
            UUID projectId,
            UUID lotId,
            CustomerInfo customerInfo,
            LeadStatus status,
            String source,
            String interestLevel,
            String notes,
            UUID assignedTo,
            LocalDateTime contactedAt,
            LocalDateTime convertedAt
    ) {
        Lead lead = new Lead(id);
        lead.tenantId = tenantId;
        lead.projectId = projectId;
        lead.lotId = lotId;
        lead.customerInfo = customerInfo;
        lead.status = status;
        lead.source = source;
        lead.interestLevel = interestLevel;
        lead.notes = notes;
        lead.assignedTo = assignedTo;
        lead.contactedAt = contactedAt;
        lead.convertedAt = convertedAt;
        return lead;
    }
}
