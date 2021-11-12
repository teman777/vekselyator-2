package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.bot.caches.operations.AddOperationCache;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.bot.services.buttons.AddButtonBuilderService;
import org.voronov.boot.core.AbstractCommand;

@Component
public class AddOperationCommand extends AbstractCommand {

    @Autowired
    private AddOperationCache cache;

    @Autowired
    private AddButtonBuilderService buttonBuilder;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageTextService messageTextService;

    @Autowired
    private Bot bot;

    public AddOperationCommand() {
        super("add", "Добавить вексель");
    }

    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        AddOperationEntity entity = new AddOperationEntity(user.getId());
        entity.setFrom(user.getId());
        entity.setChat(chat.getId());
        cache.putToCache(entity);
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, Stage.ADDING_TO);
        SendMessage message = SendMessage.builder()
                .text("Добавляем вексель")
                .replyMarkup(markup)
                .chatId(String.valueOf(chat.getId()))
                .build();
        send(message, bot);
    }

    public enum Stage {
        ADDING_TO,
        SETTING_TYPE,
        SETTING_QTY
    }
}
