package com.proyectoweb.reservations.shared_kernel;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Entity {
    protected UUID id;

    protected Entity(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity entity = (Entity) o;
        return id != null && id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
