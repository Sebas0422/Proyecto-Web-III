package com.proyectoweb.reservations.infrastructure.messaging.events;

import java.time.LocalDateTime;

public class LeadCreatedEvent {
    private Long leadId;
    private Long tenantId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String status;
    private String source;
    private LocalDateTime createdAt;

    public LeadCreatedEvent() {
    }

    public LeadCreatedEvent(Long leadId, Long tenantId, String firstName, String lastName, 
                            String email, String phoneNumber, String status, String source, 
                            LocalDateTime createdAt) {
        this.leadId = leadId;
        this.tenantId = tenantId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.source = source;
        this.createdAt = createdAt;
    }

    public Long getLeadId() {
        return leadId;
    }

    public void setLeadId(Long leadId) {
        this.leadId = leadId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
