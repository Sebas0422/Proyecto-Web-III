package com.proyectoweb.proyectos.application.commands.proyecto;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.ProyectoDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateProyectoCommand implements Command<ProyectoDto> {
    private final UUID proyectoId;
    private final UUID tenantId;
    private final String nombre;
    private final String descripcion;
    private final String ubicacion;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaEstimadaFinalizacion;

    public UpdateProyectoCommand(UUID proyectoId, UUID tenantId, String nombre, String descripcion, 
                                String ubicacion, LocalDateTime fechaInicio, LocalDateTime fechaEstimadaFinalizacion) {
        this.proyectoId = proyectoId;
        this.tenantId = tenantId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fechaInicio = fechaInicio;
        this.fechaEstimadaFinalizacion = fechaEstimadaFinalizacion;
    }

    public UUID getProyectoId() {
        return proyectoId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaEstimadaFinalizacion() {
        return fechaEstimadaFinalizacion;
    }
}
