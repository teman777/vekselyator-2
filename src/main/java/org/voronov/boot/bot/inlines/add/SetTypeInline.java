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
public class SetTypeInline extends AbstractInlineHandler<AddOperationEntity> {

    @Autowired
    private AddButtonBuilderService buttonBuilder;

    public SetTypeInline() {
        super("settype", 1);
    }

    @Override
    protected InlineHandlerChanges handle(AddOperationEntity entity, String id) {
        if (id.equals("0")) {
            entity.setType(AddOperationEntity.Type.DIVIDE_TO_ALL);
        } else if (id.equals("1")) {
            entity.setType(AddOperationEntity.Type.NOT_DIVIDE);
        }
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, AddOperationCommand.Stage.SETTING_QTY);
        return new InlineHandlerChanges(markup, "Ответь на сообщение в формате \"Сумма комментарий\"");
    }
}
