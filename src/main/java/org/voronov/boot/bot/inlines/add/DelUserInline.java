package org.voronov.boot.bot.inlines.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.operations.AddOperationCache;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.commands.AddOperationCommand;
import org.voronov.boot.bot.services.buttons.AddButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;

import java.util.UUID;

@Component
public class DelUserInline extends AbstractInlineHandler {

    @Autowired
    private AddOperationCache cache;

    @Autowired
    private AddButtonBuilderService buttonBuilder;

    public DelUserInline() {
        super("deluser");
    }

    @Override
    public BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("/");
        if (data.length == 3) {
            String userId = data[1];
            String id = data[2];
            Long chatId = callbackQuery.getMessage().getChatId();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            return inlineDelUser(userId, id, chatId, messageId);
        }
        return null;
    }

    private BotApiMethod inlineDelUser(String userId, String id, Long chatId, Integer messageId) {
        AddOperationEntity entity = cache.getFromCache(UUID.fromString(id));
        entity.deleteFromTo(Long.valueOf(userId));
        cache.putToCache(entity);
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, AddOperationCommand.Stage.ADDING_TO);
        return EditMessageReplyMarkup.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .replyMarkup(markup).build();
    }
}
