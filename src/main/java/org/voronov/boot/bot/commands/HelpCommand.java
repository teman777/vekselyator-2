package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.core.AbstractCommand;

@Component
public class HelpCommand extends AbstractCommand {

    @Autowired
    private MessageTextService textService;

    public HelpCommand() {
        super("help", "Краткий список команд");
    }


    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage sm = new SendMessage(String.valueOf(chat.getId()), textService.getHelpText());

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
