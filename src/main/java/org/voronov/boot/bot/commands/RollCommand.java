package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.core.AbstractCommand;

import java.util.Random;

@Component
public class RollCommand extends AbstractCommand {

    @Autowired
    private MessageTextService textService;

    public RollCommand() {
        super("roll", "Выброс случайного числа от 1 до 100");
    }

    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Integer random = new Random().ints(1, 100).findFirst().getAsInt();
        String brief = textService.buildBriefForUser(user);
        String msg = String.format("@%s выбросил %s", brief, random);
        SendMessage sm = SendMessage.builder()
                .text(msg)
                .chatId(chat.getId().toString())
                .build();
        send(sm, absSender);
    }
}
