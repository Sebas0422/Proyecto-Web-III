package com.proyectoweb.auth.infrastructure.persistence.adapter;

import com.proyectoweb.auth.domain.model.UserCompany;
import com.proyectoweb.auth.domain.repository.UserCompanyRepository;
import com.proyectoweb.auth.infrastructure.persistence.jpa.repository.UserCompanyJpaRepository;
import com.proyectoweb.auth.infrastructure.persistence.mapper.UserCompanyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserCompanyRepositoryAdapter implements UserCompanyRepository {

    private final UserCompanyJpaRepository jpaRepository;
    private final UserCompanyMapper mapper;

    public UserCompanyRepositoryAdapter(UserCompanyJpaRepository jpaRepository, 
                                       UserCompanyMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public UserCompany save(UserCompany userCompany) {
        var jpaModel = mapper.toJpaModel(userCompany);
        var saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<UserCompany> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<UserCompany> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserCompany> findByCompanyId(UUID companyId) {
        return jpaRepository.findByCompanyId(companyId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserCompany> findByUserIdAndCompanyId(UUID userId, UUID companyId) {
        return jpaRepository.findByUserIdAndCompanyId(userId, companyId)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndCompanyId(UUID userId, UUID companyId) {
        return jpaRepository.existsByUserIdAndCompanyId(userId, companyId);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(userCompany -> {
            userCompany.setIsDeleted(true);
            jpaRepository.save(userCompany);
        });
    }
}
