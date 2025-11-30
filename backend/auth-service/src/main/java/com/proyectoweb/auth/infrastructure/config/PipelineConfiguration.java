package com.proyectoweb.auth.infrastructure.config;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.CommandHandlers;
import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.NotificationHandlers;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineConfiguration {

    @Bean
    public Pipeline pipeline(
            ObjectProvider<Command.Handler> commandHandlers,
            ObjectProvider<Notification.Handler> notificationHandlers,
            ObjectProvider<Command.Middleware> middlewares
    ) {
        CommandHandlers commandHandlersAdapter = commandHandlers::stream;
        NotificationHandlers notificationHandlersAdapter = notificationHandlers::stream;
        Command.Middlewares middlewaresAdapter = middlewares::orderedStream;

        return new Pipelinr()
                .with(commandHandlersAdapter)
                .with(notificationHandlersAdapter)
                .with(middlewaresAdapter);
    }
}
