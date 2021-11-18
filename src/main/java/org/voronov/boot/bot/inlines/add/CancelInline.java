package org.voronov.boot.bot.inlines.add;

import org.springframework.stereotype.Component;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

import java.util.UUID;

@Component
public class CancelInline extends AbstractInlineHandler<AddOperationEntity> {

    public CancelInline() {
        super("cancel", 0);
    }

    @Override
    protected InlineHandlerChanges handle(AddOperationEntity entity, String id) {
        cache.removeFromCache(UUID.fromString(id));
        return new InlineHandlerChanges(true);
    }
}
