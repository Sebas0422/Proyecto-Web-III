package com.proyectoweb.auth.application.event_handlers;

import an.awesome.pipelinr.Notification;
import com.proyectoweb.auth.domain.events.UserRegistered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventHandler implements Notification.Handler<UserRegistered> {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredEventHandler.class);
    // TODO: Inyectar KafkaProducer

    @Override
    public void handle(UserRegistered event) {
        logger.info("Usuario registrado: {} con rol {}", event.getEmail(), event.getRole());
        
        // TODO: Publicar a Kafka topic "user-events"
        // kafkaProducer.send("user-events", event);
        
        // TODO: Enviar email de bienvenida (opcional)
    }
}
