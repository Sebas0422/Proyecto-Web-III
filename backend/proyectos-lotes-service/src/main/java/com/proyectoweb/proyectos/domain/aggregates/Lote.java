package com.proyectoweb.proyectos.domain.aggregates;

import com.proyectoweb.proyectos.domain.events.LoteCreated;
import com.proyectoweb.proyectos.domain.value_objects.EstadoLote;
import com.proyectoweb.proyectos.domain.value_objects.LoteGeometria;
import com.proyectoweb.proyectos.domain.value_objects.Precio;
import com.proyectoweb.proyectos.shared_kernel.AggregateRoot;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.UUID;

public class Lote extends AggregateRoot {
    private UUID proyectoId;
    private String numeroLote;
    private String manzana;
    private LoteGeometria geometria;
    private Double areaCalculada; // en metros cuadrados o unidades de coordenadas
    private Point centroide;
    private Precio precio;
    private EstadoLote estado;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Lote(UUID id) {
        super(id);
    }

    public static Lote crear(
            UUID id,
            UUID proyectoId,
            String numeroLote,
            String manzana,
            LoteGeometria geometria,
            Precio precio,
            String observaciones
    ) {
        if (proyectoId == null) {
            throw new IllegalArgumentException("El proyectoId no puede ser nulo");
        }
        if (numeroLote == null || numeroLote.isBlank()) {
            throw new IllegalArgumentException("El número de lote no puede estar vacío");
        }

        Lote lote = new Lote(id);
        lote.proyectoId = proyectoId;
        lote.numeroLote = numeroLote;
        lote.manzana = manzana;
        lote.geometria = geometria;
        lote.areaCalculada = geometria.calcularArea();
        lote.centroide = geometria.calcularCentroide();
        lote.precio = precio;
        lote.estado = EstadoLote.DISPONIBLE;
        lote.observaciones = observaciones;
        lote.createdAt = LocalDateTime.now();
        lote.updatedAt = LocalDateTime.now();

        lote.addDomainEvent(new LoteCreated(
                UUID.randomUUID(),
                LocalDateTime.now(),
                lote.getId(),
                lote.proyectoId,
                lote.numeroLote,
                lote.areaCalculada
        ));

        return lote;
    }

    public void actualizar(
            String numeroLote,
            String manzana,
            LoteGeometria geometria,
            Precio precio,
            String observaciones
    ) {
        if (this.estado == EstadoLote.VENDIDO) {
            throw new IllegalStateException("No se puede actualizar un lote vendido");
        }

        this.numeroLote = numeroLote != null ? numeroLote : this.numeroLote;
        this.manzana = manzana;
        
        if (geometria != null) {
            this.geometria = geometria;
            this.areaCalculada = geometria.calcularArea();
            this.centroide = geometria.calcularCentroide();
        }
        
        if (precio != null) {
            this.precio = precio;
        }
        
        this.observaciones = observaciones;
        this.updatedAt = LocalDateTime.now();
    }

    public void reservar() {
        if (this.estado != EstadoLote.DISPONIBLE) {
            throw new IllegalStateException("Solo se pueden reservar lotes disponibles");
        }
        this.estado = EstadoLote.RESERVADO;
        this.updatedAt = LocalDateTime.now();
    }

    public void marcarComoVendido() {
        if (this.estado == EstadoLote.VENDIDO) {
            throw new IllegalStateException("El lote ya está vendido");
        }
        this.estado = EstadoLote.VENDIDO;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancelarReserva() {
        if (this.estado != EstadoLote.RESERVADO) {
            throw new IllegalStateException("Solo se pueden cancelar reservas de lotes reservados");
        }
        this.estado = EstadoLote.DISPONIBLE;
        this.updatedAt = LocalDateTime.now();
    }

    public void marcarComoNoDisponible() {
        if (this.estado == EstadoLote.VENDIDO) {
            throw new IllegalStateException("No se puede marcar como no disponible un lote vendido");
        }
        this.estado = EstadoLote.NO_DISPONIBLE;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrecio(Precio nuevoPrecio) {
        if (this.estado == EstadoLote.VENDIDO) {
            throw new IllegalStateException("No se puede actualizar el precio de un lote vendido");
        }
        if (nuevoPrecio == null) {
            throw new IllegalArgumentException("El precio no puede ser nulo");
        }
        this.precio = nuevoPrecio;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEstado(EstadoLote nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        // Validaciones de transiciones de estado
        if (this.estado == EstadoLote.VENDIDO && nuevoEstado != EstadoLote.VENDIDO) {
            throw new IllegalStateException("No se puede cambiar el estado de un lote vendido");
        }
        this.estado = nuevoEstado;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateObservaciones(String nuevasObservaciones) {
        this.observaciones = nuevasObservaciones;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getProyectoId() { return proyectoId; }
    public String getNumeroLote() { return numeroLote; }
    public String getManzana() { return manzana; }
    public LoteGeometria getGeometria() { return geometria; }
    public Double getAreaCalculada() { return areaCalculada; }
    public Point getCentroide() { return centroide; }
    public Precio getPrecio() { return precio; }
    public EstadoLote getEstado() { return estado; }
    public String getObservaciones() { return observaciones; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
