package com.proyectoweb.proyectos.infrastructure.persistence.adapters;

import com.proyectoweb.proyectos.domain.aggregates.Lote;
import com.proyectoweb.proyectos.domain.repositories.LoteRepository;
import com.proyectoweb.proyectos.domain.value_objects.EstadoLote;
import com.proyectoweb.proyectos.infrastructure.persistence.jpa.repositories.LoteJpaRepository;
import com.proyectoweb.proyectos.infrastructure.persistence.mappers.LoteMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class LoteRepositoryAdapter implements LoteRepository {
    
    private final LoteJpaRepository jpaRepository;
    private final LoteMapper mapper;

    public LoteRepositoryAdapter(LoteJpaRepository jpaRepository, LoteMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Lote save(Lote lote) {
        var jpaModel = mapper.toJpa(lote);
        var saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Lote> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Lote> findByProyectoId(UUID proyectoId) {
        return jpaRepository.findByProyectoId(proyectoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lote> findByProyectoIdAndEstado(UUID proyectoId, EstadoLote estado) {
        return jpaRepository.findByProyectoIdAndEstado(proyectoId, estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByProyectoIdAndNumeroLote(UUID proyectoId, String numeroLote) {
        return jpaRepository.existsByProyectoIdAndNumeroLote(proyectoId, numeroLote);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
