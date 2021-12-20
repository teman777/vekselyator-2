package org.voronov.boot.bot.commands.administration;

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
public class RetranslateAllOffCommand extends AbstractCommand {

    @Autowired
    private Bot bot;

    @Value("${telegram.bot.mainuser}")
    private Long mainUser;

    public RetranslateAllOffCommand() {
        super("retranslationall_off", "Ретрансляция всем чатам выключена");
    }

    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (user.getId().equals(mainUser) && chat.isUserChat()) {
            bot.setTranslateAllOn(Boolean.FALSE);
            SendMessage sm = SendMessage.builder()
                    .text("Ретрансляция всем пользователям выключена.\n/retranslateall")
                    .chatId(chat.getId().toString())
                    .build();
            send(sm, absSender);
        }

    }
}
