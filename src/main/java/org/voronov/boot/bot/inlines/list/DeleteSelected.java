package org.voronov.boot.bot.inlines.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class DeleteSelected extends AbstractInlineHandler<ListOperationsEntity> {

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    @Autowired
    private ChatService chatService;

    public DeleteSelected() {
        super("listDelSl");
    }

    @Override
    protected InlineHandlerChanges handle(ListOperationsEntity entity, String id) {
        chatService.deleteOperations(entity.getSelectedOperations());
        entity.deleteSelectedOperations();

        InlineHandlerChanges changes;

        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, ListCommand.Stage.LIST_SHOW_MY);
        changes = new InlineHandlerChanges(markup);

        if (entity.isSelectedUsersEmpty()) {
            changes.setNewMsgText("Удалено");
        }

        return changes;
    }
}
