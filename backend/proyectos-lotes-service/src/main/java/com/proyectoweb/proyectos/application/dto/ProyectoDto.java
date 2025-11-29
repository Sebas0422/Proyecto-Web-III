package com.proyectoweb.proyectos.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProyectoDto(
        UUID id,
        UUID tenantId,
        String nombre,
        String descripcion,
        String ubicacion,
        LocalDateTime fechaInicio,
        LocalDateTime fechaEstimadaFinalizacion,
        boolean activo,
        LocalDateTime createdAt
) {
}
