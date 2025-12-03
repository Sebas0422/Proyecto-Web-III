package com.proyectoweb.proyectos.api.dto.requests;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class UpdateLoteRequest {
    @NotNull
    private BigDecimal precio;
    
    private String estado;
    private String observaciones;

    public UpdateLoteRequest() {
    }

    public UpdateLoteRequest(BigDecimal precio, String estado, String observaciones) {
        this.precio = precio;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
