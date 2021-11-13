package org.voronov.boot.bot.inlines.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.operations.AddOperationCache;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.commands.AddOperationCommand;
import org.voronov.boot.bot.services.buttons.AddButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;

import java.util.UUID;

@Component
public class NextInline extends AbstractInlineHandler {

    @Autowired
    private AddOperationCache cache;

    @Autowired
    private AddButtonBuilderService buttonBuilder;

    public NextInline() {
        super("next");
    }

    @Override
    public BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("/");
        if (data.length == 2) {
            String id = data[1];
            Long chatId = callbackQuery.getMessage().getChatId();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            return inlineNext(id, chatId, messageId);
        }
        return null;
    }

    private BotApiMethod inlineNext(String id, Long chatId, Integer messageId) {
        AddOperationEntity entity = cache.getFromCache(UUID.fromString(id));
        AddOperationCommand.Stage stage = entity.getTo().size() > 1
                ? AddOperationCommand.Stage.SETTING_TYPE
                : AddOperationCommand.Stage.SETTING_QTY;
        String msg;
        if (stage == AddOperationCommand.Stage.SETTING_TYPE) {
            msg = "Выбери тип векселя";
        } else {
            msg = "Ответь на сообщение в формате \"Сумма комментарий\"";
        }
        InlineKeyboardMarkup buttons = buttonBuilder.buildButtons(entity, stage);
        return EditMessageText.builder()
                .messageId(messageId)
                .replyMarkup(buttons)
                .text(msg)
                .chatId(String.valueOf(chatId))
                .build();
    }
}
