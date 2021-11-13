package org.voronov.boot.bot.caches.core;

import java.util.UUID;

public abstract class CachedEntity {
    private UUID id;
    protected Long user;

    public CachedEntity(Long user) {
        id = UUID.randomUUID();
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
