package org.voronov.boot.bot.inlines.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.bot.caches.operations.AddOperationCache;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.commands.AddOperationCommand;
import org.voronov.boot.bot.services.buttons.AddButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;

import java.util.UUID;

@Component
public class SetTypeInline extends AbstractInlineHandler {

    @Autowired
    private Bot bot;

    @Autowired
    private AddButtonBuilderService buttonBuilder;

    @Autowired
    private AddOperationCache cache;

    public SetTypeInline() {
        super("settype");
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("/");
        if (data.length == 3) {
            String type = data[1];
            String id = data[2];
            Long chatId = callbackQuery.getMessage().getChatId();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            inlineSetType(type, id, chatId, messageId);
        }
    }

    private void inlineSetType(String type, String id, Long chatId, Integer messageId) {
        AddOperationEntity entity = cache.getFromCache(UUID.fromString(id));
        if (type.equals("0")) {
            entity.setType(AddOperationEntity.Type.DIVIDE_TO_ALL);
        } else if (type.equals("1")) {
            entity.setType(AddOperationEntity.Type.NOT_DIVIDE);
        }
        cache.putToCache(entity);
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, AddOperationCommand.Stage.SETTING_QTY);
        EditMessageText text = EditMessageText.builder()
                .messageId(messageId)
                .replyMarkup(markup)
                .text("Ответь на сообщение в формате \"Сумма комментарий\"")
                .chatId(String.valueOf(chatId))
                .build();
        send(text, bot);

    }
}
