package org.voronov.boot.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.caches.core.Cache;
import org.voronov.boot.bot.caches.core.CachedEntity;

public abstract class AbstractInlineHandler<T extends CachedEntity> {

    private String inlineCommand;

    private int numberOfIds;

    @Autowired
    private AbstractInlineCommandBot bot;

    @Autowired
    protected Cache<T> cache;

    public AbstractInlineHandler(String inlineCommand, int numberOfIds) {
        this.inlineCommand = inlineCommand;
        this.numberOfIds = numberOfIds;
    }

    public String getInlineCommand() {
        return inlineCommand;
    }

    protected void handleInline(CallbackQuery callbackQuery) {
        try {
            if (checkUser(callbackQuery)) {
                String[] data = getData(callbackQuery);
                String entityId = data[data.length - 1];
                String anotherId = data[0];

                T entity = cache.getFromCache(entityId);

                InlineHandlerChanges changes = handle(entity, anotherId);
                BotApiMethod method = buildBotApi(changes, callbackQuery);

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

    protected abstract InlineHandlerChanges handle(T entity, String id);

    protected void send(BotApiMethod method, AbsSender bot) {
        try {
            bot.execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected String[] getData(CallbackQuery query) {
        String allData = query.getData();
        return allData.replace(inlineCommand + "/", "").split("/");
    }

    private BotApiMethod buildBotApi(InlineHandlerChanges changes, CallbackQuery query) {
        String chatId = query.getMessage().getChatId().toString();
        Integer messageId = query.getMessage().getMessageId();
        InlineKeyboardMarkup markup = changes.getMarkup();
        String newText = changes.getNewMsgText();
        if (newText == null && markup != null) {
            return EditMessageReplyMarkup.builder()
                    .chatId(chatId)
                    .replyMarkup(markup)
                    .messageId(messageId)
                    .build();
        } else if (newText != null && markup != null) {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(newText)
                    .replyMarkup(markup)
                    .build();
        } else if (changes.isDeleteMsg()){
            return DeleteMessage.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .build();
        }
        return null;
    }
}
