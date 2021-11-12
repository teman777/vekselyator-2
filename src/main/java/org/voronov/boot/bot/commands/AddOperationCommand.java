package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.caches.operations.AddOperationCache;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.core.AbstractCommand;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.bot.services.buttons.AddButtonBuilderService;

import java.util.UUID;

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

    public AddOperationCommand() {
        super("add", "Добавить вексель");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        super.execute(absSender, user, chat, arguments);
        AddOperationEntity entity = new AddOperationEntity();
        entity.setFrom(user.getId());
        entity.setChat(chat.getId());
        cache.putToCache(entity);
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, Stage.ADDING_TO);
        SendMessage message = SendMessage.builder()
                .text("Добавляем вексель")
                .replyMarkup(markup)
                .chatId(String.valueOf(chat.getId()))
                .build();
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

    }

    public void handleReply(Message message, AbsSender bot) {
        String text = message.getText();
        boolean check = text.matches("\\d+\\.?\\d*\\s?.*");
        if (check) {
            String onlyQty = text.replaceAll("\\s+.*", "");
            String comment = text.replaceFirst("\\d+\\.?\\d*\\s?", "");
            Double qty = Double.valueOf(onlyQty);

            String id = message.getReplyToMessage().getReplyMarkup().getKeyboard().get(0).get(0).getCallbackData().split("/")[1];
            AddOperationEntity entity = cache.getFromCache(UUID.fromString(id));

            if (message.getFrom().getId() == entity.getFrom()) {

                entity.setQty(qty);
                entity.setComment(comment);

                chatService.createOperationFromEntity(entity);

                EditMessageText edit = EditMessageText.builder()
                        .chatId(message.getChat().getId().toString())
                        .text(messageTextService.getAddOperationText())
                        .messageId(message.getReplyToMessage().getMessageId())
                        .build();

                send(edit, bot);
            } else {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(message.getChat().getId().toString())
                        .replyToMessageId(message.getMessageId())
                        .text(messageTextService.getWrongUserAddText())
                        .build();
                send(sendMessage, bot);
            }
        }
    }


    public enum Stage {
        ADDING_TO,
        SETTING_TYPE,
        SETTING_QTY
    }
}
