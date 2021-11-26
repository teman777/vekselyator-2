package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.core.AbstractCommand;

@Component
public class RetranslationCommand extends AbstractCommand {

    @Autowired
    private Bot bot;

    @Value("${telegram.bot.mainuser}")
    private Long mainUser;

    public RetranslationCommand() {
        super("retranslation", "Отправка сообщений от имени бота");
    }

    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (user.getId().equals(mainUser) && chat.isUserChat()) {
            bot.setTranslateOn(Boolean.TRUE);
            SendMessage sm = SendMessage.builder()
                    .text("Ретрансляция включена.\n/retranslation_off")
                    .chatId(chat.getId().toString())
                    .build();
            send(sm, absSender);
        }
    }
}
