package org.voronov.boot.bot.inlines.saldo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.saldo.SaldoCache;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.commands.SaldoCommand;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.buttons.SaldoButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;

import java.util.UUID;

@Component
public class SaldoPerform extends AbstractInlineHandler {

    @Autowired
    private SaldoCache cache;

    @Autowired
    private SaldoButtonBuilderService buttonBuilder;

    @Autowired
    private ChatService chatService;

    public SaldoPerform() {
        super("saldoPerform");
    }

    @Override
    protected BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = getData(callbackQuery);
        if (data.length == 2) {
            String id = data[1];

            SaldoEntity entity = cache.getFromCache(id);
            chatService.removeWithSaldo(entity);
            cache.removeFromCache(UUID.fromString(id));
            InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, SaldoCommand.Stage.YES);

            String text = "Удалено.";

            return EditMessageText.builder()
                    .chatId(callbackQuery.getMessage().getChatId().toString())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .replyMarkup(markup)
                    .text(text)
                    .build();
        }
        return null;
    }
}
