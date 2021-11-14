package org.voronov.boot.bot.inlines.saldo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.saldo.SaldoCache;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.commands.SaldoCommand;
import org.voronov.boot.bot.services.buttons.SaldoButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;

@Component
public class SelectSaldo extends AbstractInlineHandler {

    @Autowired
    private SaldoCache cache;

    @Autowired
    private SaldoButtonBuilderService buttonBuilder;


    public SelectSaldo() {
        super("saldoSel");
    }

    @Override
    protected BotApiMethod handle(CallbackQuery callbackQuery) {
        String[] data = getData(callbackQuery);
        if (data.length == 3) {
            String saldoId = data[1];
            String id = data[2];

            SaldoEntity entity = cache.getFromCache(id);
            entity.addSelectedSaldo(Long.valueOf(saldoId));
            cache.putToCache(entity);

            InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, SaldoCommand.Stage.SHOW);

            return EditMessageReplyMarkup.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(callbackQuery.getMessage().getChatId().toString())
                    .replyMarkup(markup)
                    .build();
        }
        return null;
    }
}
