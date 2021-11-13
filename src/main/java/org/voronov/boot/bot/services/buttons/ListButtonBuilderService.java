package org.voronov.boot.bot.services.buttons;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.TgUser;
import org.voronov.boot.bot.services.MessageTextService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ListButtonBuilderService {

    @Autowired
    private MessageTextService textService;

    public InlineKeyboardMarkup buildButtons(ListOperationsEntity entity, ListCommand.Stage stage) {
        if (stage == ListCommand.Stage.SETTING_TYPE) {
            return buildForSettingType(entity);
        } else if (stage == ListCommand.Stage.LIST_MY) {
            return buildForMyList(entity);
        } else if (stage == ListCommand.Stage.LIST_SHOW_ALL) {
            return buildForShow(entity);
        } else if (stage == ListCommand.Stage.LIST_SHOW_MY) {
            return buildForShowMy(entity);
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

    private InlineKeyboardMarkup buildForShowMy(ListOperationsEntity entity) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = buildButtonsForShowMy(entity);
        buttons.add(Collections.singletonList(buildCancel(entity)));
        markup.setKeyboard(buttons);
        return markup;
    }

    private List<List<InlineKeyboardButton>> buildButtonsForShowMy(ListOperationsEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        LinkedHashMap<TgUser, List<Operation>> operations = entity.getOperationsByUsers();
        for (TgUser user : operations.keySet()) {
            List<Operation> operationList = operations.get(user);
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            buttonsRow.add(buildFakeButton(user.getBrief()));
            buttonsRow.addAll(buildButtonsForShowMy(operationList, entity.getUser(), entity.getId().toString()));
            buttons.addAll(buttonsRow);
        }

        return new ArrayList<>(ListUtils.partition(buttons, 1));
    }

    private List<InlineKeyboardButton> buildButtonsForShowMy(List<Operation> operations, Long userId, String entityId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Operation operation : operations) {
            String text = operation.getuFrom().getUser().getId().equals(userId)
                    ? textService.buildOperationButtonText(operation)
                    : textService.buildOperationNegativeButtonText(operation);
            buttons.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData("selectOperation/" + operation.getId().toString() + "/" + entityId)
                    .build());
        }
        return buttons;
    }

    private InlineKeyboardButton buildFakeButton(String s) {
        return InlineKeyboardButton.builder().text(s).callbackData("nothing/").build();
    }

    private InlineKeyboardMarkup buildForShow(ListOperationsEntity entity) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(Collections.singletonList(buildCancel(entity))).build();
    }

    private InlineKeyboardMarkup buildForMyList(ListOperationsEntity entity) {
        List<List<InlineKeyboardButton>> buttons = buildUsers(entity);
        buttons.add(buildBottomRow(entity));
        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

    private List<List<InlineKeyboardButton>> buildUsers(ListOperationsEntity entity) {
        List<InlineKeyboardButton> usersButtons = new ArrayList<>();
        for (TgUser user : entity.getAnotherUsersInChat()) {
            boolean needToMark = entity.isUserSelected(user);
            usersButtons.add(buildUserButton(user, needToMark, entity.getId().toString()));
        }
        return new ArrayList<>(ListUtils.partition(usersButtons, 2));
    }

    private List<InlineKeyboardButton> buildBottomRow(ListOperationsEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(buildCancel(entity));
        if (entity.getSelectedUsers().size() > 0) {
            buttons.add(buildNextButton(entity));
        }
        return buttons;
    }

    private InlineKeyboardButton buildNextButton(ListOperationsEntity entity) {
        return InlineKeyboardButton.builder().callbackData("listNext/" + entity.getId().toString()).text("Показать").build();
    }

    private InlineKeyboardButton buildUserButton(TgUser user, boolean needToMark, String entityId) {
        return InlineKeyboardButton.builder()
                .text(needToMark
                        ? user.getBrief() + " " + new String(Character.toChars(0x2705))
                        : user.getBrief())
                .callbackData("listSelectUser/" + user.getId().toString() + "/" + entityId)
                .build();
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


        return null;
    }


}
