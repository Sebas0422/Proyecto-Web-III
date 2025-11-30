package com.proyectoweb.auth.domain.repository;

import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.value_objects.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    void delete(UUID id);
}
