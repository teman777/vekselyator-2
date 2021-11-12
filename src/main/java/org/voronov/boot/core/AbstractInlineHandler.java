package org.voronov.boot.core;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class AbstractInlineHandler {

    private String inlineCommand;

    public AbstractInlineHandler(String inlineCommand) {
        this.inlineCommand = inlineCommand;
    }

    public String getInlineCommand() {
        return inlineCommand;
    }

    protected void handleInline(CallbackQuery callbackQuery) {
        try {
            if (checkUser(callbackQuery)) {
                handle(callbackQuery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkUser(CallbackQuery query) {
        return true;
    }

    protected abstract void handle(CallbackQuery callbackQuery);

    protected void send(BotApiMethod method, AbsSender bot) {
        try {
            bot.execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
