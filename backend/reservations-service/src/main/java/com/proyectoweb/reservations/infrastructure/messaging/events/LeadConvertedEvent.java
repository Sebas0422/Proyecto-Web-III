package com.proyectoweb.reservations.infrastructure.messaging.events;

import java.time.LocalDateTime;

public class LeadConvertedEvent {
    private Long leadId;
    private Long reservationId;
    private Long tenantId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime convertedAt;

    public LeadConvertedEvent() {
    }

    public LeadConvertedEvent(Long leadId, Long reservationId, Long tenantId, String firstName, 
                              String lastName, String email, LocalDateTime convertedAt) {
        this.leadId = leadId;
        this.reservationId = reservationId;
        this.tenantId = tenantId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.convertedAt = convertedAt;
    }

    public Long getLeadId() {
        return leadId;
    }

    public void setLeadId(Long leadId) {
        this.leadId = leadId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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

    public LocalDateTime getConvertedAt() {
        return convertedAt;
    }

    public void setConvertedAt(LocalDateTime convertedAt) {
        this.convertedAt = convertedAt;
    }
}
