package org.voronov.boot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voronov.boot.bot.core.AbstractInlineCommandBot;

@Component
public class Bot extends AbstractInlineCommandBot {

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public void processNonCommandAndInlineUpdate(Update update) {
        if (update.getMessage().isReply()) {
            handleReply(update.getMessage());
        }
    }

    private void handleReply(Message message) {
        //commandService.handleReply(message, this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
