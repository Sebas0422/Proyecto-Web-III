package com.proyectoweb.proyectos.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UpdateProyectoRequest {
    
    @NotBlank(message = "El nombre del proyecto es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;
    
    private LocalDate fechaEstimadaFinalizacion;

    public UpdateProyectoRequest() {
    }

    public UpdateProyectoRequest(String nombre, String descripcion, String ubicacion, 
                                LocalDate fechaInicio, LocalDate fechaEstimadaFinalizacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fechaInicio = fechaInicio;
        this.fechaEstimadaFinalizacion = fechaEstimadaFinalizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaEstimadaFinalizacion() {
        return fechaEstimadaFinalizacion;
    }

    public void setFechaEstimadaFinalizacion(LocalDate fechaEstimadaFinalizacion) {
        this.fechaEstimadaFinalizacion = fechaEstimadaFinalizacion;
    }
}
