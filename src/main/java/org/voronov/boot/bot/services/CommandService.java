package org.voronov.boot.bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.voronov.boot.bot.commands.core.AbstractCommand;
import org.voronov.boot.bot.commands.core.InlineHandler;
import org.voronov.boot.bot.commands.core.ReplyHandler;

import java.util.Arrays;
import java.util.Collection;

@Component
public class CommandService {

    @Autowired
    private ApplicationContext context;

    public Collection<AbstractCommand> getCommands() {
        Collection<AbstractCommand> commands = context.getBeansOfType(AbstractCommand.class).values();
        return commands;
    }

    public void handleInline(CallbackQuery query, AbsSender bot) {
        String[] data = query.getData().split("/");
        String inlineCommand = data[0];
        for (AbstractCommand command : getCommands()) {
            if (command.getClass().isAnnotationPresent(InlineHandler.class)) {
                InlineHandler handler = command.getClass().getAnnotation(InlineHandler.class);
                if (Arrays.asList(handler.inlineCommands()).contains(inlineCommand)) {
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
