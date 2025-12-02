package com.proyectoweb.auth.infrastructure.messaging.events;

import java.time.LocalDateTime;

public class CompanyCreatedEvent {
    private Long tenantId;
    private String companyName;
    private String ruc;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;

    public CompanyCreatedEvent() {
    }

    public CompanyCreatedEvent(Long tenantId, String companyName, String ruc, String email, String phoneNumber, LocalDateTime createdAt) {
        this.tenantId = tenantId;
        this.companyName = companyName;
        this.ruc = ruc;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
