package org.voronov.boot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voronov.boot.bot.commands.core.AbstractCommand;
import org.voronov.boot.bot.services.CommandService;

import java.util.Collection;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingCommandBot {

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String token;

    private CommandService commandService;

    @Autowired
    public Bot(CommandService commandService) {
        this.commandService = commandService;
        registerAllCommands(commandService.getCommands());
    }

    private void registerAllCommands(Collection<AbstractCommand> commands) {
        for (AbstractCommand command : commands) {
            register(command);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            //handling callback query
            handleCallback(update);
        } else if (update.getMessage().isReply()) {
            //handling reply
            handleReply(update.getMessage());
        }
    }

    private void handleCallback(Update update) {
        CallbackQuery query = update.getCallbackQuery();
        commandService.handleInline(query, this);
    }

    private void handleReply(Message message) {
        commandService.handleReply(message, this);
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
