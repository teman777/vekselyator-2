package org.voronov.boot.core;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractInlineCommandBot extends TelegramLongPollingCommandBot {

    private final Map<String, AbstractInlineHandler> inlineHandlerMap = new HashMap<>();

    private final Map<String, AbstractReplyHandler> replyHandlerMap = new HashMap<>();

    public void registerInline(AbstractInlineHandler inlineHandler) {
        inlineHandlerMap.put(inlineHandler.getInlineCommand(), inlineHandler);
    }

    public void registerReply(AbstractReplyHandler replyHandler) {
        replyHandlerMap.put(replyHandler.getRegex(), replyHandler);
    }

    private void processInline(CallbackQuery query) {
        String command = query.getData().split("/")[0];
        AbstractInlineHandler handler = inlineHandlerMap.get(command);
        if (handler != null) {
            handler.handleInline(query);
        }
    }

    private void processReply(Message message) {
        String text = message.getText();
        for (String regex : replyHandlerMap.keySet()) {
            if (text.matches(regex)) {
                AbstractReplyHandler handler = replyHandlerMap.get(regex);
                handler.handleReply(message);
            }
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            processInline(update.getCallbackQuery());
        } else if (update.getMessage() != null && update.getMessage().isReply()) {
            processReply(update.getMessage());
        } else {
            processNonCommandAndInlineUpdate(update);
        }
    }

    public abstract void processNonCommandAndInlineUpdate(Update update);
}
