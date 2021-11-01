package org.voronov.boot.bot.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class HelpCommand extends BotCommand {

    private static final String HELP_TEXT = "Это бот векселятор - сохраняет долги в этом чате.\n" +
            "/start - Зарегистрироваться в этом чате. Можно обновить свой ник\n" +
            "/add - Добавить вексель. Жми это, если тебе задолжали.\n" +
            "/list - Меню по просмотру векселей.\n" +
            "/my - Список векселей, где ты участвуешь.\n" +
            "/all - Список вообще всех векселей в этом чате.\n" +
            "/saldo - Расчитать общее сальдо для всех участников чата.\n";

    public HelpCommand() {
        super("help", "Краткий список команд");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage sm = new SendMessage(String.valueOf(chat.getId()), HELP_TEXT);

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
