package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.bot.services.buttons.AddButtonBuilderService;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class AddOperationCommand extends BotCommand {

    @Autowired
    private AddOperationCache cache;

    @Autowired
    private AddButtonBuilderService buttonBuilder;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageTextService messageTextService;

    public static final List<String> INLINE_COMMANDS = Arrays.asList("cancel", "next", "adduser", "deluser", "settype");

    public AddOperationCommand() {
        super("add", "Добавить вексель");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
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

    public void handleInline(CallbackQuery query, AbsSender bot) {
        String[] data = query.getData().split("/");
        String entityId ;
        String anotherData;
        if (data.length > 2) {
            entityId = data[2];
            anotherData = data[1];
        } else {
            entityId = data[1];
            anotherData = "";
        }

        AddOperationEntity entity = cache.getFromCache(UUID.fromString(entityId));
        if (!query.getFrom().getId().equals(entity.getFrom())) {
            return;
        }

        switch (data[0]) {
            case "adduser":
                inlineAddUser(anotherData, entityId, query.getMessage().getChat().getId(), query.getMessage().getMessageId(), bot);
                break;
            case "deluser":
                inlineDelUser(anotherData, entityId, query.getMessage().getChat().getId(), query.getMessage().getMessageId(), bot);
                break;
            case "cancel":
                inlineCancel(entityId, query.getMessage().getChat().getId(), query.getMessage().getMessageId(), bot);
                break;
            case "next":
                inlineNext(entityId, query.getMessage().getChat().getId(), query.getMessage().getMessageId(), bot);
                break;
            case "settype":
                inlineSetType(anotherData, entityId, query.getMessage().getChat().getId(), query.getMessage().getMessageId(), bot);
                break;
            default:
                break;
        }
    }

    private void inlineAddUser(String userId, String id, Long chatId, Integer messageId, AbsSender bot) {
        AddOperationEntity entity = cache.getFromCache(UUID.fromString(id));
        entity.addTo(Long.valueOf(userId));
        cache.putToCache(entity);
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, Stage.ADDING_TO);
        EditMessageReplyMarkup edit = EditMessageReplyMarkup.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .replyMarkup(markup).build();
        send(edit, bot);
    }

    private void inlineDelUser(String userId, String id, Long chatId, Integer messageId, AbsSender bot) {
        AddOperationEntity entity = cache.getFromCache(UUID.fromString(id));
        entity.deleteFromTo(Long.valueOf(userId));
        cache.putToCache(entity);
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, Stage.ADDING_TO);
        EditMessageReplyMarkup edit = EditMessageReplyMarkup.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .replyMarkup(markup).build();
        send(edit, bot);
    }

    private void inlineSetType(String type, String id, Long chatId, Integer messageId, AbsSender bot) {
        AddOperationEntity entity = cache.getFromCache(UUID.fromString(id));
        if (type.equals("0")) {
            entity.setType(AddOperationEntity.Type.DIVIDE_TO_ALL);
        } else if (type.equals("1")) {
            entity.setType(AddOperationEntity.Type.NOT_DIVIDE);
        }
        cache.putToCache(entity);
        InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, Stage.SETTING_QTY);
        EditMessageText text = EditMessageText.builder()
                .messageId(messageId)
                .replyMarkup(markup)
                .text("Ответь на сообщение в формате \"Сумма комментарий\"")
                .chatId(String.valueOf(chatId))
                .build();
        send(text, bot);

    }

    private void inlineCancel(String id, Long chatId, Integer messageId, AbsSender bot) {
        cache.removeFromCache(UUID.fromString(id));
        DeleteMessage deleteMessage = DeleteMessage.builder().messageId(messageId).chatId(String.valueOf(chatId)).build();
        send(deleteMessage, bot);
    }

    private void inlineNext(String id, Long chatId, Integer messageId, AbsSender bot) {
        AddOperationEntity entity = cache.getFromCache(UUID.fromString(id));
        Stage stage = entity.getTo().size() > 1 ? Stage.SETTING_TYPE : Stage.SETTING_QTY;
        String msg;
        if (stage == Stage.SETTING_TYPE) {
            msg = "Выбери тип векселя";
        } else {
            msg = "Ответь на сообщение в формате \"Сумма комментарий\"";
        }
        InlineKeyboardMarkup buttons = buttonBuilder.buildButtons(entity, stage);
        EditMessageText text = EditMessageText.builder()
                .messageId(messageId)
                .replyMarkup(buttons)
                .text(msg)
                .chatId(String.valueOf(chatId))
                .build();
        send(text, bot);
    }

    private void send(BotApiMethod method, AbsSender bot) {
        try {
            bot.execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public enum Stage {
        ADDING_TO,
        SETTING_TYPE,
        SETTING_QTY
    }
}
