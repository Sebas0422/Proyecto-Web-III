package com.proyectoweb.auth.infrastructure.persistence.jpa.repository;

import com.proyectoweb.auth.infrastructure.persistence.jpa.model.CompanyJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyJpaRepository extends JpaRepository<CompanyJpaModel, UUID> {
    Optional<CompanyJpaModel> findByRuc(String ruc);
    
    @Query("SELECT c FROM CompanyJpaModel c WHERE c.isActive = true AND c.isDeleted = false")
    List<CompanyJpaModel> findAllActive();
    
    boolean existsByRuc(String ruc);
}
