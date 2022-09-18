package org.voronov.boot.bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.voronov.boot.bot.model.dto.TgChat;
import org.voronov.boot.bot.model.repositories.ChatRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ChatCache {

    private ChatRepository chatRepository;

    private Map<Long, TgChat> cache = new ConcurrentHashMap<>();

    @Autowired
    public ChatCache(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void reloadChats() {
        cache.clear();
        chatRepository.findAll().forEach(a -> cache.put(a.getId(), a));
    }

    public Optional<TgChat> findChat(Long id) {
        return Optional.ofNullable(cache.get(id));
    }

    public void updateChat(Long id) {
        Optional<TgChat> chat = chatRepository.findById(id);
        chat.ifPresent(a -> cache.put(a.getId(), a));
    }

    public List<TgChat> getAllChats() {
        return new ArrayList<>(cache.values());
    }

    public List<TgChat> getGroupChats() {
        return cache.values().stream().filter(a -> a.getId() < 0).collect(Collectors.toList());
    }
}
