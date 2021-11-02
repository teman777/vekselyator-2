package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.bot.caches.operations.AddOperationCache;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.commands.buttons.AddButtonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AddOperationCommand extends BotCommand {

    @Autowired
    private AddOperationCache cache;

    @Autowired
    private AddButtonBuilder buttonBuilder;



    public static final List<String> INLINE_COMMANDS = Arrays.asList("cancel", "next" ,"adduser", "deluser", "settype");

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

    public void handleReply(Message message, AbsSender bot) {

    }

    public void handleInline(CallbackQuery query, AbsSender bot) {
        String[] data = query.getData().split("/");
        switch (data[0]) {
            case "adduser":
                inlineAddUser(data[1], data[2]);
                break;
            case "deluser":
                inlineDelUser(data[1], data[2]);
                break;
            case "cancel":
                inlineCancel(data[1]);
                break;
            case "next":
                inlineNext(data[1]);
                break;
            case "settype":
                inlineSetType(data[1], data[2]);
                break;
            default:
                break;
        }

    }

    private void inlineAddUser(String userId, String id) {
        //todo

    }

    private void inlineDelUser(String userId, String id) {
        //todo

    }

    private void inlineSetType(String type, String id) {
        //todo

    }

    private void inlineCancel(String id) {
        //todo

    }

    private void inlineNext(String id) {

    }

    public enum Stage {
        ADDING_TO,
        SETTING_TYPE,
        SETTING_QTY
    }
}
