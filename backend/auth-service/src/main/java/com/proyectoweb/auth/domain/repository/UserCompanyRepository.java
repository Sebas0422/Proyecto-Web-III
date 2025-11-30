package com.proyectoweb.auth.domain.repository;

import com.proyectoweb.auth.domain.model.UserCompany;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserCompanyRepository {
    UserCompany save(UserCompany userCompany);
    Optional<UserCompany> findById(UUID id);
    List<UserCompany> findByUserId(UUID userId);
    List<UserCompany> findByCompanyId(UUID companyId);
    Optional<UserCompany> findByUserIdAndCompanyId(UUID userId, UUID companyId);
    boolean existsByUserIdAndCompanyId(UUID userId, UUID companyId);
    void delete(UUID id);
}
