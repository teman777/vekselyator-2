package org.voronov.boot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.services.TranslationService;
import org.voronov.boot.core.AbstractInlineCommandBot;

import java.util.List;

@Component
public class Bot extends AbstractInlineCommandBot {

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.mainuser}")
    private Long mainUser;

    @Value("${telegram.bot.mainchat}")
    private Long mainChat;

    private Boolean isTranslateOn = Boolean.FALSE;

    private Boolean isTranslateAllOn = Boolean.FALSE;

    @Autowired
    private TranslationService translationService;

    @Override
    public void processNonCommandAndInlineUpdate(Update update) {
        if (update.getMessage().getChat().isUserChat()) {
            handleRetranslation(update);
        }
    }

    private void handleRetranslation(Update update) {
        if (isTranslateAllowed(update)) {
            handleTranslate(update);
        } else if (isTranslateAllAllowed(update)) {
            handleTranslateAll(update);
        }
    }

    private void handleTranslate(Update update) {
        SendMessage sm = translationService.translate(update, mainChat);
        if (sm != null) {
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleTranslateAll(Update update) {
        List<SendMessage> messages = translationService.translateToAll(update);
        for (SendMessage sm : messages) {
            if (sm != null) {
                try {
                    execute(sm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private boolean isTranslateAllowed(Update update) {
        return isTranslateOn && update.getMessage().getFrom().getId().equals(mainUser);
    }

    private boolean isTranslateAllAllowed(Update update) {
        return isTranslateAllOn && update.getMessage().getFrom().getId().equals(mainUser);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public Boolean getTranslateOn() {
        return isTranslateOn;
    }

    public void setTranslateOn(Boolean translateOn) {
        isTranslateOn = translateOn;
    }

    public Boolean getTranslateAllOn() {
        return isTranslateAllOn;
    }

    public void setTranslateAllOn(Boolean translateAllOn) {
        isTranslateAllOn = translateAllOn;
    }
}
