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
public class My extends AbstractInlineHandler {

    @Autowired
    private ListOperationsCache cache;

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    public My() {
        super("my");
    }

    @Override
    protected BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("/");
        if (data.length == 2) {
            String id = data[1];
            ListOperationsEntity entity = cache.getFromCache(id);
            entity.setType(ListOperationsEntity.Type.MY);
            InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_MY);
            cache.putToCache(entity);
            return EditMessageReplyMarkup.builder()
                    .chatId(callbackQuery.getMessage().getChatId().toString())
                    .replyMarkup(markup)
                    .messageId(callbackQuery.getMessage().getMessageId()).build();
        }
        return null;
    }
}
