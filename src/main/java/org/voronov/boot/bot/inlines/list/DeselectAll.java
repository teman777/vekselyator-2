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
public class DeselectAll extends AbstractInlineHandler {

    @Autowired
    private ListOperationsCache cache;

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    public DeselectAll() {
        super("delAll");
    }

    @Override
    protected BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = getData(callbackQuery);
        if (data.length == 3) {
            String userId = data[1];
            String id = data[2];

            ListOperationsEntity entity = cache.getFromCache(id);
            entity.deselectAllForUser(Long.valueOf(userId));
            cache.putToCache(entity);

            InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_SHOW_MY);

            return EditMessageReplyMarkup.builder()
                    .replyMarkup(markup)
                    .chatId(callbackQuery.getMessage().getChatId().toString())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .build();
        }
        return null;
    }
}
