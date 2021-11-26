package org.voronov.boot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.core.AbstractInlineCommandBot;

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

    @Override
    public void processNonCommandAndInlineUpdate(Update update) {
        if (update.getMessage().getChat().isUserChat()) {
            handleRetranslation(update);
        }
    }

    private void handleRetranslation(Update update) {
        if (isTranslateOn && update.getMessage().getFrom().getId().equals(mainUser)) {
            SendMessage sm = SendMessage.builder()
                    .chatId(mainChat.toString())
                    .text(update.getMessage().getText())
                    .build();
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
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
}
