package org.voronov.boot.bot.services.buttons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.services.MessageTextService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ListButtonBuilderService {

    @Autowired
    private MessageTextService textService;

    public InlineKeyboardMarkup buildButtons(ListOperationsEntity entity, ListCommand.Stage stage) {
        if (stage == ListCommand.Stage.SETTING_TYPE) {
            return buildForSettingType(entity);
        } else if (stage == ListCommand.Stage.LIST_MY) {
            return buildList(entity);
        }
        return null;
    }

    private InlineKeyboardMarkup buildForSettingType(ListOperationsEntity entity) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        InlineKeyboardButton myButton = InlineKeyboardButton.builder()
                .text("Открыть мои")
                .callbackData("my/" + entity.getId().toString())
                .build();
        InlineKeyboardButton allButton = InlineKeyboardButton.builder()
                .text("Открыть все")
                .callbackData("all/" + entity.getId().toString())
                .build();

        buttonRow.add(myButton);
        buttonRow.add(allButton);
        buttons.add(buttonRow);



        buttons.add(Collections.singletonList(buildCancel(entity)));
        markup.setKeyboard(buttons);
        return markup;
    }

    private InlineKeyboardButton buildCancel(ListOperationsEntity entity) {
        return InlineKeyboardButton.builder()
                .text("Закрыть")
                .callbackData("cancelList/" + entity.getId().toString())
                .build();
    }

    private InlineKeyboardMarkup buildList(ListOperationsEntity entity) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = buildButtonsOperations(entity);
        buttons.add(Collections.singletonList(buildCancel(entity)));
        markup.setKeyboard(buttons);
        return markup;
    }

    private List<List<InlineKeyboardButton>> buildButtonsOperations(ListOperationsEntity entity) {
        List<List<InlineKeyboardButton>> operations = new ArrayList<>();

        Map<Long, Operation> map = entity.getOperationMap();
        List<Long> ids = entity.getAllSortedOperations();

        for (Long id : ids) {
            Operation operation = map.get(id);
            operations.add(Collections.singletonList(InlineKeyboardButton.builder()
                    .text(textService.getTextForOperation(operation))
                    .callbackData("select/" + operation.getId().toString() + "/" + entity.getId().toString())
                    .build()));
        }

        return operations;
    }



}
