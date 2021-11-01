package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.caches.operations.AddOperationCache;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.commands.buttons.AddButtonBuilder;

@Component
public class AddOperationCommand extends BotCommand {

    @Autowired
    private AddOperationCache cache;

    @Autowired
    private AddButtonBuilder buttonBuilder;

    public AddOperationCommand() {
        super("add", "Добавить вексель");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        AddOperationEntity entity = new AddOperationEntity();
        entity.setFrom(user.getId());
        entity.setChat(chat.getId());
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, Stage.ADDING_TO);
        SendMessage message = SendMessage.builder()
                .text("Добавляем вексель")
                .replyMarkup(markup)
                .chatId(String.valueOf(chat.getId()))
                .build();
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    public enum Stage {
        ADDING_TO,
        SETTING_TYPE,
        SETTING_QTY
    }
}
