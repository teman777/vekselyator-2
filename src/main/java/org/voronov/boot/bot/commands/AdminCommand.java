package org.voronov.boot.bot.commands;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.bot.services.buttons.AdminButtonBuilderService;
import org.voronov.boot.core.AbstractCommand;

@Component
public class AdminCommand extends AbstractCommand {

    @Autowired
    private AdminButtonBuilderService buttonBuilderService;

    @Autowired
    private Bot bot;

    @Value("${telegram.bot.mainuser}")
    private Long mainUser;

    @Autowired
    private Logger logger;

    public AdminCommand() {
        super("admin", "Панель администратора");
    }

    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (user.getId().equals(mainUser) && chat.isUserChat()) {
            InlineKeyboardMarkup keyboardMarkup = buttonBuilderService.build();
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text("Панель администратора")
                    .replyMarkup(keyboardMarkup)
                    .build();
            send(sendMessage, absSender);
        }
    }
}

