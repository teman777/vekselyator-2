package org.voronov.boot.bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.voronov.boot.bot.model.dto.TgChat;
import org.voronov.boot.bot.model.repositories.ChatRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
        TgChat chat = cache.get(id);
        if (chat == null) {
            return Optional.empty();
        } else {
            return Optional.of(chat);
        }
    }

    public void updateChat(Long id) {
        Optional<TgChat> chat = chatRepository.findById(id);
        chat.ifPresent(a -> cache.put(a.getId(), a));
    }
}
