package org.voronov.boot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voronov.boot.bot.commands.AddOperationCommand;
import org.voronov.boot.bot.commands.HelpCommand;
import org.voronov.boot.bot.commands.StartCommand;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingCommandBot {

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String token;

    private StartCommand startCommand;
    private HelpCommand helpCommand;
    private AddOperationCommand addOperationCommand;

    @Autowired
    public Bot(StartCommand startCommand, HelpCommand helpCommand, AddOperationCommand addOperationCommand) {
        this.startCommand = startCommand;
        this.helpCommand = helpCommand;
        this.addOperationCommand = addOperationCommand;
        registerAllCommands();
    }

    private void registerAllCommands() {
        register(startCommand);
        register(helpCommand);
        register(addOperationCommand);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void processNonCommandUpdate(Update update) {

    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}
