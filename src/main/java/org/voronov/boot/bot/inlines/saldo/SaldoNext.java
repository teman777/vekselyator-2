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
public class SaldoNext extends AbstractInlineHandler<SaldoEntity> {

    @Autowired
    private SaldoButtonBuilderService buttonBuilder;


    public SaldoNext() {
        super("saldoNext", 0);
    }

    @Override
    protected InlineHandlerChanges handle(SaldoEntity entity, String id) {
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, SaldoCommand.Stage.CONFIRM);
        return new InlineHandlerChanges(markup, "Внимание!\nВсе невыбранные векселя сократятся.");
    }
}
