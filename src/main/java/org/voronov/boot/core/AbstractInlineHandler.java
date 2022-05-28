package org.voronov.boot.core;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.bot.caches.core.Cache;
import org.voronov.boot.bot.caches.core.CachedEntity;
import org.voronov.boot.bot.services.ChatCache;

public abstract class AbstractInlineHandler<T extends CachedEntity> {

    private final String inlineCommand;

    private Boolean noNeedEntity = Boolean.FALSE;

    @Autowired
    private Bot bot;

    @Autowired
    private ChatCache chatCache;

    @Autowired
    protected Cache<T> cache;

    @Autowired
    private Logger logger;

    public AbstractInlineHandler(String inlineCommand) {
        this.inlineCommand = inlineCommand;
    }

    public AbstractInlineHandler(String inlineCommand, Boolean noNeedEntity) {
        this.inlineCommand = inlineCommand;
        this.noNeedEntity = noNeedEntity;
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

                T entity = null;
                if (!noNeedEntity) {
                    entity = cache.getFromCache(entityId);
                }

                InlineHandlerChanges changes = handle(entity, anotherId);
                BotApiMethod method = buildBotApi(changes, callbackQuery);

                if (method != null) {
                    send(method, bot);
                    chatCache.updateChat(callbackQuery.getMessage().getChatId());
                }
            }
        } catch (Exception e) {
            logger.error(String.format("Exception on handling inline %s", inlineCommand), e);
        }
    }

    private boolean checkUser(CallbackQuery query) {
        if (!noNeedEntity) {
            String[] data = getData(query);
            T entity = cache.getFromCache(data[data.length - 1]);
            return entity.getUser().equals(query.getFrom().getId());
        } else {
            // checking admin user
            Long admin = bot.getMainUser();
            return query.getFrom().getId().equals(admin) && query.getMessage().getChat().isUserChat();
        }
    }

    protected abstract InlineHandlerChanges handle(T entity, String id);

    protected void send(BotApiMethod method, AbsSender bot) {
        try {
            bot.execute(method);
        } catch (TelegramApiException e) {
            logger.error(String.format("Error on sending changes from %s", inlineCommand), e);
        }
    }

    protected String[] getData(CallbackQuery query) {
        String allData = query.getData();
        return allData.replace(inlineCommand + "/", "").split("/");
    }

    private BotApiMethod buildBotApi(InlineHandlerChanges changes, CallbackQuery query) {
        if (changes == null) {
            return null;
        }

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
                    .parseMode("markdown")
                    .replyMarkup(markup)
                    .build();
        } else if (changes.isDeleteMsg()) {
            return DeleteMessage.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .build();
        }
        return null;
    }
}
