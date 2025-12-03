package com.proyectoweb.auth.application.commands.user;

import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.repository.UserRepository;
import com.proyectoweb.auth.domain.value_objects.Password;
import an.awesome.pipelinr.Command;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChangePasswordCommandHandler implements Command.Handler<ChangePasswordCommand, Void> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordCommandHandler(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Void handle(ChangePasswordCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Password newPassword = Password.fromPlainText(command.newPassword());

        user.changePassword(newPassword);
        userRepository.save(user);

        return null;
    }
}
