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
public class NextInline extends AbstractInlineHandler<AddOperationEntity> {

    @Autowired
    private AddButtonBuilderService buttonBuilder;

    public NextInline() {
        super("next");
    }

    @Override
    protected InlineHandlerChanges handle(AddOperationEntity entity, String id) {
        AddOperationCommand.Stage stage = entity.getTo().size() > 1
                ? AddOperationCommand.Stage.SETTING_TYPE
                : AddOperationCommand.Stage.SETTING_QTY;

        String msg = stage == AddOperationCommand.Stage.SETTING_TYPE
                ? "Выбери тип векселя"
                : "Ответь на сообщение в формате \"Сумма комментарий\"";

        InlineKeyboardMarkup buttons = buttonBuilder.buildButtons(entity, stage);
        return new InlineHandlerChanges(buttons, msg);
    }
}
