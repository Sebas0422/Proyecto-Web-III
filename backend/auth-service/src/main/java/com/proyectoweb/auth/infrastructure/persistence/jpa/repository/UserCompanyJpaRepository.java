package com.proyectoweb.auth.infrastructure.persistence.jpa.repository;

import com.proyectoweb.auth.infrastructure.persistence.jpa.model.UserCompanyJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCompanyJpaRepository extends JpaRepository<UserCompanyJpaModel, UUID> {
    List<UserCompanyJpaModel> findByUserId(UUID userId);
    List<UserCompanyJpaModel> findByCompanyId(UUID companyId);
    Optional<UserCompanyJpaModel> findByUserIdAndCompanyId(UUID userId, UUID companyId);
    boolean existsByUserIdAndCompanyId(UUID userId, UUID companyId);
}
