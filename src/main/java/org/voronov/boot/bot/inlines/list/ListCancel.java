package org.voronov.boot.bot.inlines.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.voronov.boot.bot.caches.list.ListOperationsCache;
import org.voronov.boot.core.AbstractInlineHandler;

import java.util.UUID;

@Component
public class ListCancel extends AbstractInlineHandler {

    @Autowired
    private ListOperationsCache cache;

    public ListCancel() {
        super("cancelList");
    }

    @Override
    protected BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("/");
        if (data.length == 2) {
            String id = data[1];
            Long chatId = callbackQuery.getMessage().getChatId();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            return inlineCancel(id, chatId, messageId);
        }
        return null;
    }

    private BotApiMethod inlineCancel(String id, Long chatId, Integer messageId) {
        cache.removeFromCache(UUID.fromString(id));
        return DeleteMessage.builder().messageId(messageId).chatId(String.valueOf(chatId)).build();
    }
}
