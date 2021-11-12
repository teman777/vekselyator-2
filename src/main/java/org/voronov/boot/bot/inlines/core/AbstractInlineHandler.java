package org.voronov.boot.bot.inlines.core;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public abstract class AbstractInlineHandler {

    private String inlineCommand;

    public AbstractInlineHandler(String inlineCommand) {
        this.inlineCommand = inlineCommand;
    }

    public String getInlineCommand() {
        return inlineCommand;
    }

    public abstract void handle(CallbackQuery callbackQuery);
}
