package com.proyectoweb.proyectos.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateLoteRequest {
    
    @NotBlank(message = "El número de lote es requerido")
    private String numeroLote;
    
    private String manzana;
    
    @NotBlank(message = "La geometría WKT es requerida")
    private String geometriaWKT;
    
    @NotNull(message = "El precio es requerido")
    private BigDecimal precio;
    
    private String observaciones;

    public CreateLoteRequest() {
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getGeometriaWKT() {
        return geometriaWKT;
    }

    public void setGeometriaWKT(String geometriaWKT) {
        this.geometriaWKT = geometriaWKT;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
