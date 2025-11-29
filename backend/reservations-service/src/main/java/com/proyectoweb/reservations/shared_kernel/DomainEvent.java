package com.proyectoweb.reservations.shared_kernel;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredOn();
}
