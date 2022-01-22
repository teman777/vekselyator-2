package org.voronov.boot.bot.inlines.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class ListNext extends AbstractInlineHandler<ListOperationsEntity> {

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    @Autowired
    private MessageTextService textService;

    public ListNext() {
        super("listNext");
    }

    @Override
    protected InlineHandlerChanges handle(ListOperationsEntity entity, String id) {
        return new InlineHandlerChanges(buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_SHOW_MY));
    }
}
