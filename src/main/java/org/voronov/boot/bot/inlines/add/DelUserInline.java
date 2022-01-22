package org.voronov.boot.bot.inlines.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.commands.AddOperationCommand;
import org.voronov.boot.bot.services.buttons.AddButtonBuilderService;
import org.voronov.boot.core.AbstractInlineHandler;
import org.voronov.boot.core.InlineHandlerChanges;

@Component
public class DelUserInline extends AbstractInlineHandler<AddOperationEntity> {

    @Autowired
    private AddButtonBuilderService buttonBuilder;

    public DelUserInline() {
        super("deluser");
    }

    @Override
    protected InlineHandlerChanges handle(AddOperationEntity entity, String id) {
        entity.deleteFromTo(Long.valueOf(id));
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, AddOperationCommand.Stage.ADDING_TO);
        return new InlineHandlerChanges(markup);
    }
}
