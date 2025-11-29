package com.proyectoweb.proyectos.infrastructure.persistence.jpa.models;

import com.proyectoweb.proyectos.domain.value_objects.EstadoLote;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lotes", indexes = {
        @Index(name = "idx_proyecto_id", columnList = "proyecto_id"),
        @Index(name = "idx_proyecto_estado", columnList = "proyecto_id,estado"),
        @Index(name = "idx_numero_lote", columnList = "proyecto_id,numero_lote", unique = true)
})
public class LoteJpaModel {
    
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "proyecto_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID proyectoId;

    @Column(name = "numero_lote", nullable = false, length = 50)
    private String numeroLote;

    @Column(length = 50)
    private String manzana;

    @Column(columnDefinition = "GEOMETRY", nullable = false)
    private Polygon geometria;

    @Column(name = "area_calculada")
    private Double areaCalculada;

    @Column(columnDefinition = "POINT")
    private Point centroide;

    @Column(precision = 15, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoLote estado;

    @Column(length = 500)
    private String observaciones;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public LoteJpaModel() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getProyectoId() { return proyectoId; }
    public void setProyectoId(UUID proyectoId) { this.proyectoId = proyectoId; }

    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }

    public String getManzana() { return manzana; }
    public void setManzana(String manzana) { this.manzana = manzana; }

    public Polygon getGeometria() { return geometria; }
    public void setGeometria(Polygon geometria) { this.geometria = geometria; }

    public Double getAreaCalculada() { return areaCalculada; }
    public void setAreaCalculada(Double areaCalculada) { this.areaCalculada = areaCalculada; }

    public Point getCentroide() { return centroide; }
    public void setCentroide(Point centroide) { this.centroide = centroide; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public EstadoLote getEstado() { return estado; }
    public void setEstado(EstadoLote estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
