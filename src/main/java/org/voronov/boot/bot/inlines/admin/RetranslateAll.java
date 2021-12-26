package org.voronov.boot.bot.inlines.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.bot.caches.core.CachedEntity;
import org.voronov.boot.bot.services.buttons.AdminButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class RetranslateAll extends AbstractInlineHandler {

    @Autowired
    private AdminButtonBuilderService buttonBuilderService;

    @Autowired
    private Bot bot;

    public RetranslateAll() {
        super("retranslateAll", true);
    }

    @Override
    protected InlineHandlerChanges handle(CachedEntity entity, String id) {
        boolean onOff = "1".equals(id);
        bot.setTranslateAllOn(onOff);
        return new InlineHandlerChanges(buttonBuilderService.build());
    }
}
