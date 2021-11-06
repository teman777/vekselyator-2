package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.exceptions.NoChatException;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.MessageTextService;

@Component
public class StartCommand extends BotCommand {
    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageTextService messageTextService;

    public StartCommand() {
        super("start", "Регистрация нового пользователя");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String brief = user.getUserName();
        if (brief == null || "".equals(brief)) {
            brief = user.getFirstName();
            if (user.getLastName() != null && !user.getLastName().equals("")) {
                brief += " " + user.getLastName();
            }
        }

        SendMessage sm;

        try {
            chatService.registerUserForChat(chat.getId(), user.getId(), brief);
            sm = new SendMessage(String.valueOf(chat.getId()), messageTextService.getRegisterText(brief));
        } catch (NoChatException e) {
            //todo logging
            e.printStackTrace();
            sm = new SendMessage(String.valueOf(chat.getId()), "Произошла какая-то ебала, я вас не зарегал :(");
        }

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
