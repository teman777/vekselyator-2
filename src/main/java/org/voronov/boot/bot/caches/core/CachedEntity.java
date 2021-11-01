package org.voronov.boot.bot.caches.core;

import java.util.UUID;

public abstract class CachedEntity {
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
