package com.proyectoweb.proyectos.infrastructure.persistence.mappers;

import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.value_objects.Descripcion;
import com.proyectoweb.proyectos.domain.value_objects.ProyectoNombre;
import com.proyectoweb.proyectos.domain.value_objects.Ubicacion;
import com.proyectoweb.proyectos.infrastructure.persistence.jpa.models.ProyectoJpaModel;
import org.springframework.stereotype.Component;

@Component
public class ProyectoMapper {
    
    public ProyectoJpaModel toJpa(Proyecto proyecto) {
        ProyectoJpaModel jpa = new ProyectoJpaModel();
        jpa.setId(proyecto.getId());
        jpa.setTenantId(proyecto.getTenantId());
        jpa.setNombre(proyecto.getNombre().value());
        jpa.setDescripcion(proyecto.getDescripcion() != null ? proyecto.getDescripcion().value() : null);
        jpa.setUbicacion(proyecto.getUbicacion().value());
        jpa.setFechaInicio(proyecto.getFechaInicio());
        jpa.setFechaEstimadaFinalizacion(proyecto.getFechaEstimadaFinalizacion());
        jpa.setActivo(proyecto.isActivo());
        jpa.setCreatedAt(proyecto.getCreatedAt());
        jpa.setUpdatedAt(proyecto.getUpdatedAt());
        return jpa;
    }

    public Proyecto toDomain(ProyectoJpaModel jpa) {
        return Proyecto.crear(
                jpa.getId(),
                jpa.getTenantId(),
                new ProyectoNombre(jpa.getNombre()),
                jpa.getDescripcion() != null ? new Descripcion(jpa.getDescripcion()) : null,
                new Ubicacion(jpa.getUbicacion()),
                jpa.getFechaInicio(),
                jpa.getFechaEstimadaFinalizacion()
        );
    }
}
