package org.voronov.boot.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.Bot;

public abstract class AbstractInlineHandler {

    private String inlineCommand;

    @Autowired
    private Bot bot;

    public AbstractInlineHandler(String inlineCommand) {
        this.inlineCommand = inlineCommand;
    }

    public String getInlineCommand() {
        return inlineCommand;
    }

    protected void handleInline(CallbackQuery callbackQuery) {
        try {
            if (checkUser(callbackQuery)) {
                BotApiMethod method = handle(callbackQuery);
                if (method != null) {
                    send(method, bot);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkUser(CallbackQuery query) {
        return true;
    }

    protected abstract BotApiMethod handle(CallbackQuery callbackQuery);

    protected void send(BotApiMethod method, AbsSender bot) {
        try {
            bot.execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected String[] getData(CallbackQuery query) {
        return query.getData().split("/");
    }
}
