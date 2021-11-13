package org.voronov.boot.bot.inlines.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.list.ListOperationsCache;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;

@Component
public class ListDeselectUser extends AbstractInlineHandler {

    @Autowired
    private ListOperationsCache cache;

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    public ListDeselectUser() {
        super("listDeselectUser");
    }

    @Override
    protected BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = getData(callbackQuery);
        EditMessageReplyMarkup editMessageReplyMarkup = null;
        if (data.length == 3) {
            String userId = data[1];
            String id = data[2];

            ListOperationsEntity entity = cache.getFromCache(id);
            entity.deleteSelectedUser(Long.valueOf(userId));
            InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_MY);
            cache.putToCache(entity);

            editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                    .chatId(callbackQuery.getMessage().getChatId().toString())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .replyMarkup(markup)
                    .build();
        }
        return editMessageReplyMarkup;
    }
}
