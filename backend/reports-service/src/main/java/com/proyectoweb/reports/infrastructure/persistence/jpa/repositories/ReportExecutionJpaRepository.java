package com.proyectoweb.reports.infrastructure.persistence.jpa.repositories;

import com.proyectoweb.reports.infrastructure.persistence.jpa.models.ReportExecutionJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportExecutionJpaRepository extends JpaRepository<ReportExecutionJpaModel, String> {
    
    List<ReportExecutionJpaModel> findByTenantIdOrderByRequestedAtDesc(String tenantId);
    
    List<ReportExecutionJpaModel> findByTenantIdAndReportTypeOrderByRequestedAtDesc(String tenantId, String reportType);
}
