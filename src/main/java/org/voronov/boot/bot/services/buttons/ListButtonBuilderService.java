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

import java.util.*;

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

        if (!entity.isNothingToShowMy()) {
            InlineKeyboardButton myButton = InlineKeyboardButton.builder()
                    .text("Открыть мои")
                    .callbackData("my/" + entity.getId().toString())
                    .build();
            buttonRow.add(myButton);
        }

        if (!entity.isNothingToShowAll()) {
            InlineKeyboardButton allButton = InlineKeyboardButton.builder()
                    .text("Открыть все")
                    .callbackData("all/" + entity.getId().toString())
                    .build();
            buttonRow.add(allButton);
        }
        if (!buttonRow.isEmpty()) {
            buttons.add(buttonRow);
        }
        buttons.add(Collections.singletonList(buildCancel(entity)));
        markup.setKeyboard(buttons);
        return markup;
    }

    private InlineKeyboardMarkup buildForShowMy(ListOperationsEntity entity) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = buildButtonsForShowMy(entity);
        List<InlineKeyboardButton> bottomButtons = buildBottomForShowMy(entity);
        buttons.add(bottomButtons);
        markup.setKeyboard(buttons);
        return markup;
    }

    private List<InlineKeyboardButton> buildBottomForShowMy(ListOperationsEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(buildCancel(entity));
        if (!entity.getSelectedOperations().isEmpty()) {
            buttons.add(buildDeleteButton(entity));
        }
        return buttons;
    }

    private InlineKeyboardButton buildDeleteButton(ListOperationsEntity entity) {
        return InlineKeyboardButton.builder()
                .callbackData("listDelSl/" + entity.getId().toString())
                .text("Удалить")
                .build();
    }

    private List<List<InlineKeyboardButton>> buildButtonsForShowMy(ListOperationsEntity entity) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        LinkedHashMap<TgUser, List<Operation>> operations = entity.getOperationsBySelectedUsers();
        Optional<TgUser> currentUser = entity.getSelectedTgUser();
        if (currentUser.isPresent()) {
            TgUser user = currentUser.get();
            List<Operation> operationList = operations.get(user);

            buttons.add(buildUserRowUnselected(entity));

            buttons.add(Collections.singletonList(buildSelectAllForUser(user, entity.getId().toString(), entity.isAllSelectedForCurrent(), entity.getBalanceForCurrent())));
            List<InlineKeyboardButton> operationsButton = buildButtonsForShowMy(operationList, entity.getSelectedOperations(), entity.getUser(), entity.getId().toString());
            buttons.addAll(new ArrayList<>(ListUtils.partition(operationsButton, 1)));
        }

        return buttons;
    }

    private List<InlineKeyboardButton> buildUserRowUnselected(ListOperationsEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (TgUser tgUser : entity.getUnselectedUsers()) {
            buttons.add(InlineKeyboardButton.builder()
                    .text(tgUser.getBrief())
                    .callbackData("selCurUs/" + tgUser.getId().toString() + "/" + entity.getId().toString())
                    .build());
        }
        return buttons;
    }

    private List<InlineKeyboardButton> buildButtonsForShowMy(List<Operation> operations, List<Long> selected, Long userId, String entityId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Operation operation : operations) {
            String text = operation.getuFrom().getUser().getId().equals(userId)
                    ? textService.buildOperationButtonText(operation)
                    : textService.buildOperationNegativeButtonText(operation);

            String callback = "selOp/" + operation.getId().toString() + "/" + entityId;
            if (selected.contains(operation.getId())) {
                text += " " + new String(Character.toChars(0x2705));
                callback = callback.replace("selOp/", "dslOp/");
            }

            buttons.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(callback)
                    .build());
        }
        return buttons;
    }

    private InlineKeyboardButton buildSelectAllForUser(TgUser user, String id, boolean isAllSelected, Double qty) {
        String text = !isAllSelected
                ? String.format("(%.2f)", qty) + "⬇ " + user.getBrief()
                : String.format("(%.2f)", qty) + "⬇ " + user.getBrief() + " " + new String(Character.toChars(0x2705));
        String callback = !isAllSelected
                ? "selAll/" + user.getId().toString() + "/" + id
                : "delAll/" + user.getId().toString() + "/" + id;

        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callback).build();
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
        for (TgUser user : entity.getUsersHaveOperations()) {
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
                .callbackData(!needToMark
                        ? "listSelectUser/" + user.getId().toString() + "/" + entityId
                        : "listDeselectUser/" + user.getId().toString() + "/" + entityId)
                .build();
    }

    private InlineKeyboardButton buildCancel(ListOperationsEntity entity) {
        return InlineKeyboardButton.builder()
                .text("Закрыть")
                .callbackData("cancelList/" + entity.getId().toString())
                .build();
    }
}
