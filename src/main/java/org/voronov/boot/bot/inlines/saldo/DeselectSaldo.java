package org.voronov.boot.bot.inlines.saldo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.commands.SaldoCommand;
import org.voronov.boot.bot.services.buttons.SaldoButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class DeselectSaldo extends AbstractInlineHandler<SaldoEntity> {

    @Autowired
    private SaldoButtonBuilderService buttonBuilder;

    public DeselectSaldo() {
        super("saldoDesel", 1);
    }

    @Override
    protected InlineHandlerChanges handle(SaldoEntity entity, String saldoId) {
        entity.removeSelectedSaldo(Long.valueOf(saldoId));
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, SaldoCommand.Stage.SHOW);
        return new InlineHandlerChanges(markup);
    }
}
