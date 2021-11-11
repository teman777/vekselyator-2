package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.voronov.boot.bot.commands.core.AbstractCommand;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.MessageTextService;

import java.util.Arrays;
import java.util.List;

@Component
public class StartCommand extends AbstractCommand {
    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageTextService messageTextService;

    public static final List<String> INLINE_COMMANDS = Arrays.asList("ready", "register");

    public StartCommand() {
        super("start", "Регистрация нового пользователя");
    }

    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage sm;
        String brief = messageTextService.buildBriefForUser(user);
        try {
            chatService.registerUserForChat(chat.getId(), user.getId(), brief);
            sm = new SendMessage(String.valueOf(chat.getId()), messageTextService.getRegisterText(brief));
        } catch (Exception e) {
            //todo logging
            e.printStackTrace();
            sm = new SendMessage(String.valueOf(chat.getId()), messageTextService.getError());
        }
        send(sm, absSender);
    }

    public void handleInline(CallbackQuery query, AbsSender bot) {


    }

}
