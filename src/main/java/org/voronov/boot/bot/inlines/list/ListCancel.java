package org.voronov.boot.bot.inlines.list;

import org.springframework.stereotype.Component;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class ListCancel extends AbstractInlineHandler<ListOperationsEntity> {

    public ListCancel() {
        super("cancelList");
    }

    @Override
    protected InlineHandlerChanges handle(ListOperationsEntity entity, String id) {
        cache.removeFromCache(entity.getId());
        return new InlineHandlerChanges(true);
    }
}
