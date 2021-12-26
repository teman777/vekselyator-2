package org.voronov.boot.bot.inlines.saldo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.services.buttons.SaldoButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class SaldoClose extends AbstractInlineHandler<SaldoEntity> {

    @Autowired
    private SaldoButtonBuilderService buttonBuilder;

    public SaldoClose() {
        super("saldoClose");
    }

    @Override
    protected InlineHandlerChanges handle(SaldoEntity entity, String id) {
        cache.removeFromCache(entity.getId());
        return new InlineHandlerChanges(true);
    }
}
