package org.voronov.boot.bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.voronov.boot.bot.commands.AddOperationCommand;
import org.voronov.boot.bot.commands.HelpCommand;
import org.voronov.boot.bot.commands.ListCommand;
import org.voronov.boot.bot.commands.StartCommand;
import org.voronov.boot.bot.commands.core.AbstractCommand;
import org.voronov.boot.bot.commands.core.InlineHandler;
import org.voronov.boot.bot.commands.core.ReplyHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CommandService {

    @Autowired
    private StartCommand startCommand;

    @Autowired
    private AddOperationCommand addOperationCommand;

    @Autowired
    private HelpCommand helpCommand;

    @Autowired
    private ListCommand listCommand;

    public List<AbstractCommand> getCommands() {
        List<AbstractCommand> commands = new ArrayList<>();
        commands.add(startCommand);
        commands.add(helpCommand);
        commands.add(listCommand);
        commands.add(addOperationCommand);
        return commands;
    }

    public void handleInline(CallbackQuery query, AbsSender bot) {
        String[] data = query.getData().split("/");
        String inlineCommand = data[0];
        for (AbstractCommand command : getCommands()) {
            if (command.getClass().isAnnotationPresent(InlineHandler.class)) {
                InlineHandler handler = command.getClass().getAnnotation(InlineHandler.class);
                if (Arrays.stream(handler.inlineCommands()).anyMatch(a -> a.equals(inlineCommand))) {
                    command.handleInline(query, bot);
                }
            }
        }
    }

    public void handleReply(Message message, AbsSender bot) {
        for (AbstractCommand command : getCommands()) {
            if (command.getClass().isAnnotationPresent(ReplyHandler.class)) {
                ReplyHandler handler = command.getClass().getAnnotation(ReplyHandler.class);
                command.handleReply(message, bot);
            }
        }
    }
}
