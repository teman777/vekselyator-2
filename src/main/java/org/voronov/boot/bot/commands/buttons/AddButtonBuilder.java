package org.voronov.boot.bot.commands.buttons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.commands.AddOperationCommand;
import org.voronov.boot.bot.model.dto.TgChat;
import org.voronov.boot.bot.model.dto.TgUser;
import org.voronov.boot.bot.model.dto.UserChat;
import org.voronov.boot.bot.model.repositories.ChatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AddButtonBuilder {

    @Autowired
    private ChatRepository chatRepository;

    public InlineKeyboardMarkup buildButtons(AddOperationEntity entity, AddOperationCommand.Stage stage) {
        switch (stage) {
            case ADDING_TO:
                return buildAddingTo(entity);
            case SETTING_TYPE:
                return buildSettingType(entity);
            case SETTING_QTY:
                return buildSettingQty(entity);
            default:
                return null;
        }
    }

    private InlineKeyboardMarkup buildAddingTo(AddOperationEntity entity) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        result.add(buildUsers(entity));
        result.add(buildStageButtons(entity));
        markup.setKeyboard(result);
        return markup;
    }

    private InlineKeyboardMarkup buildSettingType(AddOperationEntity entity) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        result.add(buildTypes(entity));
        result.add(buildOnlyCancel(entity));
        markup.setKeyboard(result);
        return markup;
    }

    private InlineKeyboardMarkup buildSettingQty(AddOperationEntity entity) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        result.add(buildOnlyCancel(entity));
        markup.setKeyboard(result);
        return markup;
    }

    private List<InlineKeyboardButton> buildTypes(AddOperationEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton firstType = new InlineKeyboardButton("Делим на всех");
        firstType.setCallbackData("settype/0/" + entity.getId().toString());
        InlineKeyboardButton secondType = new InlineKeyboardButton("С каждого по");
        secondType.setCallbackData("settype/1/" + entity.getId().toString());
        buttons.add(firstType);
        buttons.add(secondType);
        return buttons;
    }

    private List<InlineKeyboardButton> buildOnlyCancel(AddOperationEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton cancel = new InlineKeyboardButton("Отмена");
        cancel.setCallbackData("cancel/" + entity.getId().toString());
        buttons.add(cancel);
        return buttons;
    }

    private List<InlineKeyboardButton> buildUsers(AddOperationEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        Optional<TgChat> chat = chatRepository.findById(entity.getChat());
        if (chat.isPresent()) {
            List<TgUser> users = chat.get().getUsers()
                    .stream()
                    .map(UserChat::getUser)
                    .collect(Collectors.toList());
            for (TgUser user : users) {
                String text = user.getBrief();
                String callback = "adduser/" + user.getId().toString() + "/" + entity.getId().toString();
                if (entity.getTo().contains(user.getId())) {
                    text += " " + new String(Character.toChars(0x2705));
                    callback.replace("adduser", "deluser");
                }
                InlineKeyboardButton button = new InlineKeyboardButton(text);
                button.setCallbackData(callback);
                buttons.add(button);
            }
        }
        return buttons;
    }

    private List<InlineKeyboardButton> buildStageButtons(AddOperationEntity entity) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton cancelButton = new InlineKeyboardButton("Отмена");
        cancelButton.setCallbackData("cancel/" + entity.getId().toString());
        buttons.add(cancelButton);

        if (!entity.getTo().isEmpty()) {
            InlineKeyboardButton continueButton = new InlineKeyboardButton("Далее");
            continueButton.setCallbackData("next/" + entity.getId().toString());
            buttons.add(continueButton);
        }

        return buttons;

    }

}
