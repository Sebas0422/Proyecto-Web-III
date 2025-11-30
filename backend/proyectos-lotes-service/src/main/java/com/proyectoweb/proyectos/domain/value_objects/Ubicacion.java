package com.proyectoweb.proyectos.domain.value_objects;

import com.proyectoweb.proyectos.shared_kernel.ValueObject;

public record Ubicacion(String value) {
    public Ubicacion {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("La ubicación del proyecto no puede estar vacía");
        }
        if (value.length() > 500) {
            throw new IllegalArgumentException("La ubicación no puede exceder 500 caracteres");
        }
    }
}
