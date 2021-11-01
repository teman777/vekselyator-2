package org.voronov.boot.bot.caches.core;


import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Cache<T extends CachedEntity> {
    private ConcurrentHashMap<UUID, T> cacheMap;

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
}
