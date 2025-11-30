package com.proyectoweb.proyectos.application.queries.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteDto;

import java.util.List;
import java.util.UUID;

public record GetLotesByProyectoQuery(
        UUID proyectoId,
        String estado
) implements Command<List<LoteDto>> {
}
