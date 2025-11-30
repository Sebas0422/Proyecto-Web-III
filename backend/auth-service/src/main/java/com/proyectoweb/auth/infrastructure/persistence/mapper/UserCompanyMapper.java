package com.proyectoweb.auth.infrastructure.persistence.mapper;

import com.proyectoweb.auth.domain.model.UserCompany;
import com.proyectoweb.auth.domain.value_objects.Role;
import com.proyectoweb.auth.infrastructure.persistence.jpa.model.UserCompanyJpaModel;
import com.proyectoweb.auth.infrastructure.persistence.jpa.model.UserJpaModel;
import org.springframework.stereotype.Component;

@Component
public class UserCompanyMapper {

    public UserCompanyJpaModel toJpaModel(UserCompany userCompany) {
        UserCompanyJpaModel jpaModel = new UserCompanyJpaModel();
        jpaModel.setId(userCompany.getId());
        jpaModel.setUserId(userCompany.getUserId());
        jpaModel.setCompanyId(userCompany.getCompanyId());
        jpaModel.setRole(UserJpaModel.RoleEnum.valueOf(userCompany.getRole().name()));
        jpaModel.setIsActive(userCompany.isActive());
        jpaModel.setIsDeleted(userCompany.isDeleted());
        jpaModel.setCreatedAt(userCompany.getCreatedAt());
        jpaModel.setUpdatedAt(userCompany.getUpdatedAt());
        return jpaModel;
    }

    public UserCompany toDomain(UserCompanyJpaModel jpaModel) {
        return UserCompany.reconstitute(
            jpaModel.getId(),
            jpaModel.getUserId(),
            jpaModel.getCompanyId(),
            Role.valueOf(jpaModel.getRole().name()),
            jpaModel.getIsActive()
        );
    }
}
