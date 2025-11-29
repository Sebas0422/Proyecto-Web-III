package com.proyectoweb.auth.domain.value_objects;

public enum Role {
    CLIENTE,
    EMPRESA,
    ADMIN;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isEmpresa() {
        return this == EMPRESA;
    }

    public boolean isCliente() {
        return this == CLIENTE;
    }
}
