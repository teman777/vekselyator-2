package org.voronov.boot.bot.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;
import java.util.List;

@Component
public class ListCommand extends BotCommand {

    public static final List<String> INLINE_COMMANDS = Arrays.asList("cancelList", "nextList", "prevList", "my", "all", "select", "deleteOperations");

    public ListCommand() {
        super("list", "Список векселей");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

    }

    public void handleInline(CallbackQuery query, AbsSender bot) {

    }

    public enum Type {
        MY, ALL;
    }

    public enum Stage {
        SETTING_TYPE,
        LIST,
        SETTING_QTY
    }
}
