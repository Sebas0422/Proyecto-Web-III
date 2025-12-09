package com.proyectoweb.auth.domain.value_objects;

import com.proyectoweb.auth.shared_kernel.core.BusinessRule;
import com.proyectoweb.auth.shared_kernel.core.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Email extends ValueObject {
    private final String value;

    public Email(String value) {
        checkRule(new EmailValidationRule(value));
        this.value = value.toLowerCase().trim();
    }

    @Override
    public String toString() {
        return value;
    }

    private static class EmailValidationRule implements BusinessRule {
        private final String email;
        private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        public EmailValidationRule(String email) {
            this.email = email;
        }

        @Override
        public boolean isValid() {
            return email != null && !email.trim().isEmpty() && email.matches(EMAIL_REGEX);
        }

        @Override
        public String getMessage() {
            return "El email no es válido: " + email;
        }
    }
}
