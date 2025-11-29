package com.proyectoweb.proyectos.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record LoteDto(
        UUID id,
        UUID proyectoId,
        String numeroLote,
        String manzana,
        String geometriaWKT,
        Double areaCalculada,
        String centroideWKT,
        BigDecimal precio,
        String estado,
        String observaciones,
        LocalDateTime createdAt
) {
}
