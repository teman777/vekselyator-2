package org.voronov.boot.bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voronov.boot.bot.model.dto.TgChat;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TranslationService {

    @Autowired
    private ChatCache chatCache;


    public SendMessage translate(Update update, Long mainChat) {
        SendMessage sm = SendMessage.builder()
                .chatId(mainChat.toString())
                .text(update.getMessage().getText())
                .build();
        return sm;
    }

    public List<SendMessage> translateToAll(Update update, Long admin) {
        String text = update.getMessage().getText();
        List<TgChat> chats = chatCache.getAllChats().stream()
                .filter(a -> !a.getId().equals(admin))
                .collect(Collectors.toList());
        return chats.stream().map(a -> {
            SendMessage sm = SendMessage.builder()
                    .text(text)
                    .chatId(a.getId().toString())
                    .build();
            return sm;
        }).collect(Collectors.toList());


    }
}
