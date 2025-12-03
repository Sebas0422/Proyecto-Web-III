package com.proyectoweb.auth.application.commands.user;

import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.repository.UserRepository;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeactivateUserCommandHandler implements Command.Handler<DeactivateUserCommand, Void> {

    private final UserRepository userRepository;

    public DeactivateUserCommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Void handle(DeactivateUserCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.deactivate();
        userRepository.save(user);

        return null;
    }
}
