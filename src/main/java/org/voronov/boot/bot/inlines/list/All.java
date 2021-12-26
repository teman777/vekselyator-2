package org.voronov.boot.bot.inlines.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class All extends AbstractInlineHandler<ListOperationsEntity> {

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    @Autowired
    private MessageTextService textService;

    public All() {
        super("all");
    }

    @Override
    protected InlineHandlerChanges handle(ListOperationsEntity entity, String id) {
        entity.setType(ListOperationsEntity.Type.ALL);
        String msg = textService.buildOperationTextForMessage(entity.getOperationMap().values());
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_SHOW_ALL);
        return new InlineHandlerChanges(markup, msg);
    }
}
