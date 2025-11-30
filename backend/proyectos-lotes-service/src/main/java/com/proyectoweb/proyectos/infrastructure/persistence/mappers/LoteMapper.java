package com.proyectoweb.proyectos.infrastructure.persistence.mappers;

import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.value_objects.LoteGeometria;
import com.proyectoweb.proyectos.domain.value_objects.Precio;
import com.proyectoweb.proyectos.infrastructure.persistence.jpa.models.LoteJpaModel;
import org.springframework.stereotype.Component;

@Component
public class LoteMapper {
    
    public LoteJpaModel toJpa(Lote lote) {
        LoteJpaModel jpa = new LoteJpaModel();
        jpa.setId(lote.getId());
        jpa.setProyectoId(lote.getProyectoId());
        jpa.setNumeroLote(lote.getNumeroLote());
        jpa.setManzana(lote.getManzana());
        jpa.setGeometria(lote.getGeometria().polygon());
        jpa.setAreaCalculada(lote.getAreaCalculada());
        jpa.setCentroide(lote.getCentroide());
        jpa.setPrecio(lote.getPrecio().value());
        jpa.setEstado(lote.getEstado());
        jpa.setObservaciones(lote.getObservaciones());
        jpa.setCreatedAt(lote.getCreatedAt());
        jpa.setUpdatedAt(lote.getUpdatedAt());
        return jpa;
    }

    public Lote toDomain(LoteJpaModel jpa) {
        return Lote.crear(
                jpa.getId(),
                jpa.getProyectoId(),
                jpa.getNumeroLote(),
                jpa.getManzana(),
                new LoteGeometria(jpa.getGeometria()),
                new Precio(jpa.getPrecio()),
                jpa.getObservaciones()
        );
    }
}
