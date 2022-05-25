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
public class PrevInline extends AbstractInlineHandler<AddOperationEntity> {

    @Autowired
    private AddButtonBuilderService buttonBuilder;

    public PrevInline() {
        super("prev");
    }

    @Override
    protected InlineHandlerChanges handle(AddOperationEntity entity, String id) {
        if (id.equals("1") || id.equals("2")) {
            InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, id.equals("1")
                    ? AddOperationCommand.Stage.ADDING_TO
                    : AddOperationCommand.Stage.SETTING_TYPE);
            return new InlineHandlerChanges(markup);
        }
        return null;
    }
}
