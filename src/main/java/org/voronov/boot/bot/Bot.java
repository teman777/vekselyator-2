package org.voronov.boot.bot;

import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voronov.boot.bot.commands.AddOperationCommand;
import org.voronov.boot.bot.commands.HelpCommand;
import org.voronov.boot.bot.commands.ListCommand;
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
    private ListCommand listCommand;

    @Autowired
    public Bot(StartCommand startCommand, HelpCommand helpCommand, AddOperationCommand addOperationCommand, ListCommand listCommand) {
        this.startCommand = startCommand;
        this.helpCommand = helpCommand;
        this.addOperationCommand = addOperationCommand;
        this.listCommand = listCommand;
        registerAllCommands();
    }

    private void registerAllCommands() {
        register(startCommand);
        register(helpCommand);
        register(addOperationCommand);
        register(listCommand);
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
        String command = query.getData().split("/")[0];
        if (StartCommand.INLINE_COMMANDS.contains(command)) {
            startCommand.handleInline(query, this);
        } else if (AddOperationCommand.INLINE_COMMANDS.contains(command)){
            addOperationCommand.handleInline(query, this);
        } else if (ListCommand.INLINE_COMMANDS.contains(command)) {
            listCommand.handleInline(query, this);
        }

    }

    private void handleReply(Message message) {
        addOperationCommand.handleReply(message, this);

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
