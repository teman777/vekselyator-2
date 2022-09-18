package org.voronov.boot.bot.caches.core;


import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache<T extends CachedEntity> {
    private final ConcurrentHashMap<UUID, T> cacheMap;

    public Cache() {
        cacheMap = new ConcurrentHashMap<>();
    }

    public T putToCache(T entity) {
        UUID id = entity.getId();
        if (id == null) {
            id = UUID.randomUUID();
            entity.setId(id);
        }
        cacheMap.put(id, entity);
        return entity;
    }

    public void removeFromCache(T entity) {
        UUID id = entity.getId();
        if (id != null) {
            cacheMap.remove(id);
        }
    }

    public void removeFromCache(UUID id) {
        if (id != null) {
            cacheMap.remove(id);
        }
    }

    public T getFromCache(UUID id) {
        if (id != null) {
            return cacheMap.get(id);
        }
        return null;
    }

    public T getFromCache(String id) {
        if (id != null) {
            return cacheMap.get(UUID.fromString(id));
        }
        return null;
    }
}
