package com.proyectoweb.auth.domain.model;

import com.proyectoweb.auth.domain.events.CompanyCreated;
import com.proyectoweb.auth.shared_kernel.core.AggregateRoot;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Company extends AggregateRoot {
    private String name;
    private String ruc;
    private String address;
    private String phoneNumber;
    private String email;
    private String logoUrl;
    private boolean isActive;

    // Constructor privado
    private Company(UUID id, String name, String ruc, String address, String phoneNumber, 
                   String email, String logoUrl) {
        super(id);
        this.name = name;
        this.ruc = ruc;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.logoUrl = logoUrl;
        this.isActive = true;
    }

    // Factory method
    public static Company create(String name, String ruc, String address, 
                                String phoneNumber, String email) {
        validateCompanyData(name, ruc);
        
        UUID companyId = UUID.randomUUID();
        Company company = new Company(companyId, name, ruc, address, phoneNumber, email, null);
        
        company.addDomainEvent(new CompanyCreated(companyId, name, ruc));
        
        return company;
    }

    // Reconstituir desde BD
    public static Company reconstitute(UUID id, String name, String ruc, String address,
                                      String phoneNumber, String email, String logoUrl,
                                      boolean isActive) {
        Company company = new Company(id, name, ruc, address, phoneNumber, email, logoUrl);
        company.isActive = isActive;
        return company;
    }

    // Métodos de negocio
    public void updateInfo(String name, String address, String phoneNumber, String email) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (address != null) {
            this.address = address;
        }
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
        if (email != null) {
            this.email = email;
        }
        updateTimestamp();
    }

    public void updateLogo(String logoUrl) {
        this.logoUrl = logoUrl;
        updateTimestamp();
    }

    public void activate() {
        this.isActive = true;
        updateTimestamp();
    }

    public void deactivate() {
        this.isActive = false;
        updateTimestamp();
    }

    // Validaciones
    private static void validateCompanyData(String name, String ruc) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la empresa es obligatorio");
        }
        if (ruc == null || ruc.trim().isEmpty()) {
            throw new IllegalArgumentException("El RUC es obligatorio");
        }
        if (ruc.length() < 8) {
            throw new IllegalArgumentException("El RUC debe tener al menos 8 caracteres");
        }
    }
}
