package org.voronov.boot.core;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class AbstractReplyHandler {

    private String regex;

    @Autowired
    private Logger logger;

    public AbstractReplyHandler(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    protected void send(BotApiMethod method, AbsSender bot) {
        try {
            bot.execute(method);
        } catch (TelegramApiException e) {
            logger.error(String.format("Error on send msg from reply %s", getClass().getSimpleName()), e);
        }
    }

    public abstract void handleReply(Message message);

}
