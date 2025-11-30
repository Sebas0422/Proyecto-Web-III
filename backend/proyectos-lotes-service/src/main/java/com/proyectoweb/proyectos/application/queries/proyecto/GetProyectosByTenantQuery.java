package com.proyectoweb.proyectos.application.queries.proyecto;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;

import java.util.List;
import java.util.UUID;

public record GetProyectosByTenantQuery(
        UUID tenantId,
        Boolean soloActivos
) implements Command<List<ProyectoDto>> {
}
