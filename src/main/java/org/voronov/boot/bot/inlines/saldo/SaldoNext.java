package org.voronov.boot.bot.inlines.saldo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.commands.SaldoCommand;
import org.voronov.boot.bot.services.SaldoService;
import org.voronov.boot.bot.services.buttons.SaldoButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class SaldoNext extends AbstractInlineHandler<SaldoEntity> {

    @Autowired
    private SaldoButtonBuilderService buttonBuilder;

    @Autowired
    private SaldoService saldoService;

    public SaldoNext() {
        super("saldoNext");
    }

    @Override
    protected InlineHandlerChanges handle(SaldoEntity entity, String id) {
        saldoService.considerWhatShouldBeDeleted(entity);
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, SaldoCommand.Stage.CONFIRM);
        return new InlineHandlerChanges(markup, "Внимание!\nВсе невыбранные векселя сократятся.");
    }
}
