package org.voronov.boot.bot.inlines.admin;

import org.springframework.stereotype.Component;
import org.voronov.boot.bot.caches.core.CachedEntity;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class AdminClose extends AbstractInlineHandler {
    public AdminClose() {
        super("adminClose", true);
    }

    @Override
    protected InlineHandlerChanges handle(CachedEntity entity, String id) {
        return new InlineHandlerChanges(true);
    }
}
