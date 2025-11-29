package com.proyectoweb.proyectos.domain.value_objects;

import com.proyectoweb.proyectos.shared_kernel.ValueObject;

public record ProyectoNombre(String value) {
    public ProyectoNombre {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El nombre del proyecto no puede estar vacío");
        }
        if (value.length() < 3 || value.length() > 200) {
            throw new IllegalArgumentException("El nombre del proyecto debe tener entre 3 y 200 caracteres");
        }
    }
}
