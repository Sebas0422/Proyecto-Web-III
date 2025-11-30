package com.proyectoweb.proyectos.infrastructure.persistence.adapters;

import com.proyectoweb.proyectos.domain.aggregates.Proyecto;
import com.proyectoweb.proyectos.domain.repositories.ProyectoRepository;
import com.proyectoweb.proyectos.infrastructure.persistence.jpa.repositories.ProyectoJpaRepository;
import com.proyectoweb.proyectos.infrastructure.persistence.mappers.ProyectoMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProyectoRepositoryAdapter implements ProyectoRepository {
    
    private final ProyectoJpaRepository jpaRepository;
    private final ProyectoMapper mapper;

    public ProyectoRepositoryAdapter(ProyectoJpaRepository jpaRepository, ProyectoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Proyecto save(Proyecto proyecto) {
        var jpaModel = mapper.toJpa(proyecto);
        var saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Proyecto> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Proyecto> findByTenantId(UUID tenantId) {
        return jpaRepository.findByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Proyecto> findByTenantIdAndActivo(UUID tenantId, boolean activo) {
        return jpaRepository.findByTenantIdAndActivo(tenantId, activo).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
