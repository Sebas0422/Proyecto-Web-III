package com.proyectoweb.auth.domain.value_objects;

import com.proyectoweb.auth.shared_kernel.core.ValueObject;
import lombok.Getter;

@Getter
public class Password extends ValueObject {
    private final String hashedValue;

    private Password(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    public static Password fromPlainText(String plainPassword) {
        // Validación antes de hashear
        new PasswordValidationRule(plainPassword).validate();
        // En Infrastructure se hasheará con BCrypt
        return new Password(plainPassword);
    }

    public static Password fromHash(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        return new Password(hashedPassword);
    }

    private static class PasswordValidationRule implements com.proyectoweb.auth.shared_kernel.core.BusinessRule {
        private final String password;
        private static final int MIN_LENGTH = 6;

        public PasswordValidationRule(String password) {
            this.password = password;
        }

        @Override
        public boolean isValid() {
            return password != null && password.length() >= MIN_LENGTH;
        }

        @Override
        public String getMessage() {
            return "La contraseña debe tener al menos " + MIN_LENGTH + " caracteres";
        }

        public void validate() {
            if (!isValid()) {
                throw new com.proyectoweb.auth.shared_kernel.core.BusinessRuleValidationException(this);
            }
        }
    }

    @Override
    public String toString() {
        return "***PROTECTED***";
    }
}
