package com.proyectoweb.payments.infrastructure.config;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineConfiguration {

    @Bean
    Pipeline commandPipeline(ObjectProvider<Command.Handler> commandHandlers) {
        return new Pipelinr()
                .with(() -> commandHandlers.stream());
    }
}
