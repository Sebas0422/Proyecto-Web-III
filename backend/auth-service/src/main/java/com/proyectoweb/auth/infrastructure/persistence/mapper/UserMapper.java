package com.proyectoweb.auth.infrastructure.persistence.mapper;

import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.value_objects.Email;
import com.proyectoweb.auth.domain.value_objects.Password;
import com.proyectoweb.auth.domain.value_objects.Role;
import com.proyectoweb.auth.infrastructure.persistence.jpa.model.UserJpaModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserJpaModel toJpaModel(User user) {
        UserJpaModel jpaModel = new UserJpaModel();
        jpaModel.setId(user.getId());
        jpaModel.setEmail(user.getEmail().getValue());
        jpaModel.setPasswordHash(user.getPassword().getHashedValue());
        jpaModel.setFirstName(user.getFirstName());
        jpaModel.setLastName(user.getLastName());
        jpaModel.setPhoneNumber(user.getPhoneNumber());
        jpaModel.setDefaultRole(UserJpaModel.RoleEnum.valueOf(user.getDefaultRole().name()));
        jpaModel.setIsActive(user.isActive());
        jpaModel.setIsEmailVerified(user.isEmailVerified());
        jpaModel.setIsDeleted(user.isDeleted());
        jpaModel.setCreatedAt(user.getCreatedAt());
        jpaModel.setUpdatedAt(user.getUpdatedAt());
        return jpaModel;
    }

    public User toDomain(UserJpaModel jpaModel) {
        return User.reconstitute(
            jpaModel.getId(),
            new Email(jpaModel.getEmail()),
            Password.fromHash(jpaModel.getPasswordHash()),
            jpaModel.getFirstName(),
            jpaModel.getLastName(),
            jpaModel.getPhoneNumber(),
            Role.valueOf(jpaModel.getDefaultRole().name()),
            jpaModel.getIsActive(),
            jpaModel.getIsEmailVerified()
        );
    }
}
