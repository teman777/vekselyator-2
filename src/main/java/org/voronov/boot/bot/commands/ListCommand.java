package org.voronov.boot.bot.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class ListCommand extends BotCommand {

    public ListCommand() {
        super("list", "Список векселей");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

    }
}
