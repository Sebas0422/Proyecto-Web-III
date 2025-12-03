package com.proyectoweb.proyectos.application.commands.lote;

import an.awesome.pipelinr.Command;
import com.proyectoweb.proyectos.application.dto.LoteDto;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateLoteCommand implements Command<LoteDto> {
    private final UUID loteId;
    private final UUID tenantId;
    private final BigDecimal precio;
    private final String estado;
    private final String observaciones;

    public UpdateLoteCommand(UUID loteId, UUID tenantId, BigDecimal precio, String estado, String observaciones) {
        this.loteId = loteId;
        this.tenantId = tenantId;
        this.precio = precio;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public UUID getLoteId() {
        return loteId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
