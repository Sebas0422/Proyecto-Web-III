package com.proyectoweb.auth.application.use_cases.company.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CompanyDto {
    private final UUID id;
    private final String name;
    private final String ruc;
    private final String address;
    private final String phoneNumber;
    private final String email;
    private final String logoUrl;
    private final boolean isActive;
}
