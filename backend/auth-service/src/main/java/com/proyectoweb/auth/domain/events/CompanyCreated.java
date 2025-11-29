package com.proyectoweb.auth.domain.events;

import com.proyectoweb.auth.shared_kernel.core.DomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CompanyCreated extends DomainEvent {
    private final UUID companyId;
    private final String companyName;
    private final String ruc;

    public CompanyCreated(UUID companyId, String companyName, String ruc) {
        super();
        this.companyId = companyId;
        this.companyName = companyName;
        this.ruc = ruc;
    }
}
