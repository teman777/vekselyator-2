package org.voronov.boot.bot.core;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voronov.boot.bot.inlines.core.AbstractInlineHandler;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractInlineCommandBot extends TelegramLongPollingCommandBot {

    private Map<String, AbstractInlineHandler> inlineHandlerMap = new HashMap<>();

    public void registerInline(AbstractInlineHandler inlineHandler) {
        inlineHandlerMap.put(inlineHandler.getInlineCommand(), inlineHandler);
    }

    private void processInline(CallbackQuery query) {
        String command = query.getData().split("/")[0];
        AbstractInlineHandler handler = inlineHandlerMap.get(command);
        if (handler != null) {
            handler.handle(query);
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            processInline(update.getCallbackQuery());
        } else {
            processNonCommandAndInlineUpdate(update);
        }
    }

    public abstract void processNonCommandAndInlineUpdate(Update update);
}
