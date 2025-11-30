package com.proyectoweb.proyectos.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CreateProyectoRequest {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    private String nombre;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @NotBlank(message = "La ubicación es requerida")
    @Size(max = 500, message = "La ubicación no puede exceder 500 caracteres")
    private String ubicacion;
    
    @NotNull(message = "La fecha de inicio es requerida")
    private LocalDate fechaInicio;
    
    private LocalDate fechaEstimadaFinalizacion;

    public CreateProyectoRequest() {
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
