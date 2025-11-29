package com.proyectoweb.auth.infrastructure.persistence.adapter;

import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.repository.UserRepository;
import com.proyectoweb.auth.domain.value_objects.Email;
import com.proyectoweb.auth.infrastructure.persistence.jpa.model.UserJpaModel;
import com.proyectoweb.auth.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.proyectoweb.auth.infrastructure.persistence.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository, 
                                UserMapper mapper,
                                PasswordEncoder passwordEncoder) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        UserJpaModel jpaModel = mapper.toJpaModel(user);
        
        // Si la contraseña no está hasheada (viene de Password.fromPlainText), hashearla
        if (!jpaModel.getPasswordHash().startsWith("$2a$")) {
            String hashedPassword = passwordEncoder.encode(jpaModel.getPasswordHash());
            jpaModel.setPasswordHash(hashedPassword);
        }
        
        UserJpaModel saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.findById(id).ifPresent(user -> {
            user.setIsDeleted(true);
            jpaRepository.save(user);
        });
    }
}
