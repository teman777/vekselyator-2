package org.voronov.boot.bot.inlines.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class ListSelectUser extends AbstractInlineHandler<ListOperationsEntity> {

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    public ListSelectUser() {
        super("listSelectUser", 1);
    }

    @Override
    protected InlineHandlerChanges handle(ListOperationsEntity entity, String userId) {
        entity.addSelectedUser(Long.valueOf(userId));
        InlineKeyboardMarkup buttons = buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_MY);
        return new InlineHandlerChanges(buttons);
    }
}
