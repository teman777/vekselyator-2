package org.voronov.boot.bot.reply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.voronov.boot.bot.Bot;
import org.voronov.boot.bot.caches.core.Cache;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.services.ChatCache;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.core.AbstractReplyHandler;

import java.util.UUID;

@Component
public class AddOperationReply extends AbstractReplyHandler {

    @Autowired
    private Bot bot;

    @Autowired
    private Cache<AddOperationEntity> cache;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatCache chatCache;

    @Autowired
    private MessageTextService messageTextService;

    public AddOperationReply() {
        super("\\d+\\.?\\d*\\s?.*");
    }

    @Override
    public void handleReply(Message message) {
        String text = message.getText();
        String onlyQty = text.replaceAll("\\s+.*", "");
        String comment = text.replaceFirst("\\d+\\.?\\d*\\s?", "");
        Double qty = Double.valueOf(onlyQty);

        String id = message.getReplyToMessage().getReplyMarkup().getKeyboard().get(0).get(0).getCallbackData().split("/")[1];
        AddOperationEntity entity = cache.getFromCache(UUID.fromString(id));

        if (message.getFrom().getId() == entity.getFrom()) {

            entity.setQty(qty);
            entity.setComment(comment);

            chatService.createOperationFromEntity(entity);
            chatCache.updateChat(message.getChatId());
            EditMessageText edit = EditMessageText.builder()
                    .chatId(message.getChat().getId().toString())
                    .text(messageTextService.getAddOperationText())
                    .messageId(message.getReplyToMessage().getMessageId())
                    .build();

            send(edit, bot);
        }
    }
}
