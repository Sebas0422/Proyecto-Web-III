package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteDto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateLoteCommand(
        UUID proyectoId,
        String numeroLote,
        String manzana,
        String geometriaWKT,
        BigDecimal precio,
        String observaciones
) implements Command<LoteDto> {
}
