package com.proyectoweb.auth.infrastructure.persistence.adapter;

import com.proyectoweb.auth.domain.model.Company;
import com.proyectoweb.auth.domain.repository.CompanyRepository;
import com.proyectoweb.auth.infrastructure.persistence.jpa.repository.CompanyJpaRepository;
import com.proyectoweb.auth.infrastructure.persistence.mapper.CompanyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final CompanyJpaRepository jpaRepository;
    private final CompanyMapper mapper;

    public CompanyRepositoryAdapter(CompanyJpaRepository jpaRepository, CompanyMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Company save(Company company) {
        var jpaModel = mapper.toJpaModel(company);
        var saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Company> findByRuc(String ruc) {
        return jpaRepository.findByRuc(ruc)
                .map(mapper::toDomain);
    }

    @Override
    public List<Company> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Company> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByRuc(String ruc) {
        return jpaRepository.existsByRuc(ruc);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(company -> {
            company.setIsDeleted(true);
            jpaRepository.save(company);
        });
    }
}
