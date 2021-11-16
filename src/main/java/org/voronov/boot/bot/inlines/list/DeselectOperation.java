package org.voronov.boot.bot.inlines.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.core.Cache;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class DeselectOperation extends AbstractInlineHandler<ListOperationsEntity>{

    @Autowired
    private Cache<ListOperationsEntity> cache;

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    public DeselectOperation() {
        super("dslOp", 1);
    }

    @Override
    protected InlineHandlerChanges handle(ListOperationsEntity entity, String id) {
        entity.deselectOperation(Long.valueOf(id));
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_SHOW_MY);
        return new InlineHandlerChanges(markup);
    }
}
