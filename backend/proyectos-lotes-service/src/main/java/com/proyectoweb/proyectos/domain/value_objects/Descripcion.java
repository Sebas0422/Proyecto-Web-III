package com.proyectoweb.proyectos.domain.value_objects;

import com.proyectoweb.proyectos.shared_kernel.ValueObject;

public record Descripcion(String value) {
    public Descripcion {
        if (value != null && value.length() > 1000) {
            throw new IllegalArgumentException("La descripción no puede exceder 1000 caracteres");
        }
    }
}
