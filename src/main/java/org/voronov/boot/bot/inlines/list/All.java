package org.voronov.boot.bot.inlines.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.list.ListOperationsCache;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;

@Component
public class All extends AbstractInlineHandler {

    @Autowired
    private ListOperationsCache cache;

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    @Autowired
    private MessageTextService textService;

    public All() {
        super("all");
    }

    @Override
    protected BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = getData(callbackQuery);
        if (data.length == 2) {
            String id = data[1];
            ListOperationsEntity entity = cache.getFromCache(id);
            entity.setType(ListOperationsEntity.Type.ALL);

            cache.putToCache(entity);

            String msg = textService.buildOperationTextForMessage(entity.getOperationMap().values());
            InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_SHOW_ALL);

            return EditMessageText.builder()
                    .text(msg)
                    .replyMarkup(markup)
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(callbackQuery.getMessage().getChatId().toString())
                    .build();
        }
        return null;
    }
}
