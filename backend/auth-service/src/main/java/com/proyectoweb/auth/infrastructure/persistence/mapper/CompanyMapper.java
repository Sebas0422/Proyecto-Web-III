package com.proyectoweb.auth.infrastructure.persistence.mapper;

import com.proyectoweb.auth.domain.model.Company;
import com.proyectoweb.auth.infrastructure.persistence.jpa.model.CompanyJpaModel;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public CompanyJpaModel toJpaModel(Company company) {
        CompanyJpaModel jpaModel = new CompanyJpaModel();
        jpaModel.setId(company.getId());
        jpaModel.setName(company.getName());
        jpaModel.setRuc(company.getRuc());
        jpaModel.setAddress(company.getAddress());
        jpaModel.setPhoneNumber(company.getPhoneNumber());
        jpaModel.setEmail(company.getEmail());
        jpaModel.setLogoUrl(company.getLogoUrl());
        jpaModel.setIsActive(company.isActive());
        jpaModel.setIsDeleted(company.isDeleted());
        jpaModel.setCreatedAt(company.getCreatedAt());
        jpaModel.setUpdatedAt(company.getUpdatedAt());
        return jpaModel;
    }

    public Company toDomain(CompanyJpaModel jpaModel) {
        return Company.reconstitute(
            jpaModel.getId(),
            jpaModel.getName(),
            jpaModel.getRuc(),
            jpaModel.getAddress(),
            jpaModel.getPhoneNumber(),
            jpaModel.getEmail(),
            jpaModel.getLogoUrl(),
            jpaModel.getIsActive()
        );
    }
}
