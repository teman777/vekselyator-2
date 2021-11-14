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
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;

@Component
public class DeleteSelected extends AbstractInlineHandler {

    @Autowired
    private ListOperationsCache cache;

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    @Autowired
    private ChatService chatService;

    public DeleteSelected() {
        super("listDelSl");
    }

    @Override
    protected BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = getData(callbackQuery);
        if (data.length == 2) {
            String id = data[1];

            ListOperationsEntity entity = cache.getFromCache(id);
            chatService.deleteOperations(entity.getSelectedOperations());
            entity.deleteSelectedOperations();
            cache.putToCache(entity);

            InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_SHOW_MY);

            return EditMessageReplyMarkup.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(callbackQuery.getMessage().getChatId().toString())
                    .replyMarkup(markup)
                    .build();
        }
        return null;
    }
}
