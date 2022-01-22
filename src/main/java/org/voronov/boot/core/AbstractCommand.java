package org.voronov.boot.core;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.services.ChatCache;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.MessageTextService;

public abstract class AbstractCommand extends BotCommand {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageTextService messageTextService;

    @Autowired
    protected ChatCache chatCache;

    @Autowired
    protected Logger logger;

    public AbstractCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String brief = messageTextService.buildBriefForUser(user);
        chatService.registerUserForChat(chat.getId(), user.getId(), brief, chat.getTitle());
        try {
            __execute(absSender, user, chat, arguments);
            chatCache.updateChat(chat.getId());
        } catch (Exception e) {
            logger.error(String.format("Error on executing command %s", super.getCommandIdentifier()), e);
        }
    }

    protected void send(BotApiMethod method, AbsSender bot) {
        try {
            bot.execute(method);
        } catch (TelegramApiException e) {
            logger.error(String.format("Error on send msg from command %s", super.getCommandIdentifier()), e);
        }
    }

    protected abstract void __execute(AbsSender absSender, User user, Chat chat, String[] arguments);
}
