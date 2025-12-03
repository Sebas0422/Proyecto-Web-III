package com.proyectoweb.proyectos.domain.aggregates;

import com.proyectoweb.proyectos.domain.events.ProyectoCreated;
import com.proyectoweb.proyectos.domain.value_objects.Descripcion;
import com.proyectoweb.proyectos.domain.value_objects.ProyectoNombre;
import com.proyectoweb.proyectos.domain.value_objects.Ubicacion;
import com.proyectoweb.proyectos.shared_kernel.AggregateRoot;

import java.time.LocalDateTime;
import java.util.UUID;

public class Proyecto extends AggregateRoot {
    private UUID tenantId;
    private ProyectoNombre nombre;
    private Descripcion descripcion;
    private Ubicacion ubicacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaEstimadaFinalizacion;
    private boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Proyecto(UUID id) {
        super(id);
    }

    public static Proyecto crear(
            UUID id,
            UUID tenantId,
            ProyectoNombre nombre,
            Descripcion descripcion,
            Ubicacion ubicacion,
            LocalDateTime fechaInicio,
            LocalDateTime fechaEstimadaFinalizacion
    ) {
        if (tenantId == null) {
            throw new IllegalArgumentException("El tenantId no puede ser nulo");
        }
        
        if (fechaEstimadaFinalizacion != null && fechaEstimadaFinalizacion.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha estimada de finalización no puede ser anterior a la fecha de inicio");
        }

        Proyecto proyecto = new Proyecto(id);
        proyecto.tenantId = tenantId;
        proyecto.nombre = nombre;
        proyecto.descripcion = descripcion;
        proyecto.ubicacion = ubicacion;
        proyecto.fechaInicio = fechaInicio;
        proyecto.fechaEstimadaFinalizacion = fechaEstimadaFinalizacion;
        proyecto.activo = true;
        proyecto.createdAt = LocalDateTime.now();
        proyecto.updatedAt = LocalDateTime.now();

        proyecto.addDomainEvent(new ProyectoCreated(
                UUID.randomUUID(),
                LocalDateTime.now(),
                proyecto.getId(),
                proyecto.tenantId,
                proyecto.nombre.value()
        ));

        return proyecto;
    }

    public void actualizar(
            ProyectoNombre nombre,
            Descripcion descripcion,
            Ubicacion ubicacion,
            LocalDateTime fechaEstimadaFinalizacion
    ) {
        if (fechaEstimadaFinalizacion != null && fechaEstimadaFinalizacion.isBefore(this.fechaInicio)) {
            throw new IllegalArgumentException("La fecha estimada de finalización no puede ser anterior a la fecha de inicio");
        }

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fechaEstimadaFinalizacion = fechaEstimadaFinalizacion;
        this.updatedAt = LocalDateTime.now();
    }

    public void desactivar() {
        this.activo = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activar() {
        this.activo = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.activo = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateNombre(ProyectoNombre nombre) {
        this.nombre = nombre;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescripcion(Descripcion descripcion) {
        this.descripcion = descripcion;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateFechaInicio(LocalDateTime fechaInicio) {
        if (this.fechaEstimadaFinalizacion != null && fechaInicio.isAfter(this.fechaEstimadaFinalizacion)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha estimada de finalización");
        }
        this.fechaInicio = fechaInicio;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateFechaEstimadaFinalizacion(LocalDateTime fechaEstimadaFinalizacion) {
        if (fechaEstimadaFinalizacion != null && fechaEstimadaFinalizacion.isBefore(this.fechaInicio)) {
            throw new IllegalArgumentException("La fecha estimada de finalización no puede ser anterior a la fecha de inicio");
        }
        this.fechaEstimadaFinalizacion = fechaEstimadaFinalizacion;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getTenantId() { return tenantId; }
    public ProyectoNombre getNombre() { return nombre; }
    public Descripcion getDescripcion() { return descripcion; }
    public Ubicacion getUbicacion() { return ubicacion; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaEstimadaFinalizacion() { return fechaEstimadaFinalizacion; }
    public boolean isActivo() { return activo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
