package org.voronov.boot.bot.inlines.saldo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.commands.SaldoCommand;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.buttons.SaldoButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class SaldoPerform extends AbstractInlineHandler<SaldoEntity> {

    @Autowired
    private SaldoButtonBuilderService buttonBuilder;

    @Autowired
    private ChatService chatService;

    public SaldoPerform() {
        super("saldoPerform", 0);
    }

    @Override
    protected InlineHandlerChanges handle(SaldoEntity entity, String id) {
        chatService.removeWithSaldo(entity);
        //cache.removeFromCache(UUID.fromString(id));
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, SaldoCommand.Stage.YES);

        String text = "Удалено.";
        return new InlineHandlerChanges(markup, text);
    }
}
