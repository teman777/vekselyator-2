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
public class AddUserInline extends AbstractInlineHandler<AddOperationEntity> {

    @Autowired
    private AddButtonBuilderService buttonService;

    public AddUserInline() {
        super("adduser", 1);
    }

    @Override
    protected InlineHandlerChanges handle(AddOperationEntity entity, String id) {
        entity.addTo(Long.valueOf(id));
        InlineKeyboardMarkup markup = buttonService.buildButtons(entity, AddOperationCommand.Stage.ADDING_TO);
        return new InlineHandlerChanges(markup);
    }
}
