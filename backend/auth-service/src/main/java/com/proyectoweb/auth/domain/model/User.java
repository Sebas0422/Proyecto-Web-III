package com.proyectoweb.auth.domain.model;

import com.proyectoweb.auth.domain.events.UserRegistered;
import com.proyectoweb.auth.domain.value_objects.Email;
import com.proyectoweb.auth.domain.value_objects.Password;
import com.proyectoweb.auth.domain.value_objects.Role;
import com.proyectoweb.auth.shared_kernel.core.AggregateRoot;
import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends AggregateRoot {
    private Email email;
    private Password password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role defaultRole;
    private boolean isActive;
    private boolean isEmailVerified;

    // Constructor privado para crear entidad
    private User(UUID id, Email email, Password password, String firstName, String lastName, 
                 String phoneNumber, Role defaultRole) {
        super(id);
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.defaultRole = defaultRole;
        this.isActive = true;
        this.isEmailVerified = false;
    }

    // Factory method - Registro de nuevo usuario
    public static User register(Email email, Password password, String firstName, 
                               String lastName, String phoneNumber, Role role) {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, email, password, firstName, lastName, phoneNumber, role);
        
        // Evento de dominio
        user.addDomainEvent(new UserRegistered(userId, email.getValue(), role.name()));
        
        return user;
    }

    // Reconstituir desde BD
    public static User reconstitute(UUID id, Email email, Password password, String firstName,
                                   String lastName, String phoneNumber, Role defaultRole,
                                   boolean isActive, boolean isEmailVerified) {
        User user = new User(id, email, password, firstName, lastName, phoneNumber, defaultRole);
        user.isActive = isActive;
        user.isEmailVerified = isEmailVerified;
        return user;
    }

    // Métodos de negocio
    public void updateProfile(String firstName, String lastName, String phoneNumber) {
        if (firstName != null && !firstName.trim().isEmpty()) {
            this.firstName = firstName;
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            this.lastName = lastName;
        }
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
        updateTimestamp();
    }

    public void changePassword(Password newPassword) {
        this.password = newPassword;
        updateTimestamp();
    }

    public void verifyEmail() {
        this.isEmailVerified = true;
        updateTimestamp();
    }

    public void activate() {
        this.isActive = true;
        updateTimestamp();
    }

    public void deactivate() {
        this.isActive = false;
        updateTimestamp();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean canLogin() {
        return isActive && !isDeleted();
    }
}
