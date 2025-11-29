package com.proyectoweb.proyectos.domain.value_objects;

import com.proyectoweb.proyectos.shared_kernel.ValueObject;

import java.math.BigDecimal;

public record Precio(BigDecimal value) {
    public Precio {
        if (value == null) {
            throw new IllegalArgumentException("El precio no puede ser nulo");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }
}
