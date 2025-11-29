package com.proyectoweb.proyectos.application.commands.proyecto;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProyectoCommand(
        UUID tenantId,
        String nombre,
        String descripcion,
        String ubicacion,
        LocalDateTime fechaInicio,
        LocalDateTime fechaEstimadaFinalizacion
) implements Command<ProyectoDto> {
}
