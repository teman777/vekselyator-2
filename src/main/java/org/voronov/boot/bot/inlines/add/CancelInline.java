package org.voronov.boot.bot.inlines.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.bot.caches.operations.AddOperationCache;
import org.voronov.boot.core.AbstractInlineHandler;

import java.util.UUID;

@Component
public class CancelInline extends AbstractInlineHandler {

    @Autowired
    private AddOperationCache cache;

    @Autowired
    private Bot bot;

    public CancelInline() {
        super("cancel");
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("/");
        if (data.length == 2) {
            String id = data[1];
            Long chatId = callbackQuery.getMessage().getChatId();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            inlineCancel(id, chatId, messageId);
        }

    }

    private void inlineCancel(String id, Long chatId, Integer messageId) {
        cache.removeFromCache(UUID.fromString(id));
        DeleteMessage deleteMessage = DeleteMessage.builder().messageId(messageId).chatId(String.valueOf(chatId)).build();
        send(deleteMessage, bot);
    }
}
