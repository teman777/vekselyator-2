package org.voronov.boot.core;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class AbstractReplyHandler {

    private String regex;
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
            e.printStackTrace();
        }
    }

    public abstract void handleReply(Message message);

}
