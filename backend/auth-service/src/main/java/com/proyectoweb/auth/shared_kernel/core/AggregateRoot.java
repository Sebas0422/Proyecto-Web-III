package com.proyectoweb.auth.shared_kernel.core;

public abstract class AggregateRoot extends Entity {
    protected AggregateRoot() {
        super();
    }

    protected AggregateRoot(java.util.UUID id) {
        super(id);
    }
}
