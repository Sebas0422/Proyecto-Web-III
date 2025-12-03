package com.proyectoweb.auth.application.commands.user;

import com.proyectoweb.auth.domain.model.User;
import com.proyectoweb.auth.domain.repository.UserRepository;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ActivateUserCommandHandler implements Command.Handler<ActivateUserCommand, Void> {

    private final UserRepository userRepository;

    public ActivateUserCommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Void handle(ActivateUserCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.activate();
        userRepository.save(user);

        return null;
    }
}
