package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.voronov.boot.bot.caches.list.ListOperationsCache;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.core.AbstractCommand;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.TgChat;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ListCommand extends AbstractCommand {

    @Autowired
    private ListButtonBuilderService buttonBuilder;

    @Autowired
    private ListOperationsCache cache;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageTextService textService;

    public ListCommand() {
        super("list", "Список векселей");
    }

    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Optional<TgChat> findedChat = chatService.findChat(chat.getId());
        if (findedChat.isPresent()) {
            TgChat tgChat = findedChat.get();
            Set<Operation> operations = tgChat.getAllChatsOperations();
            List<Long> allOperations = tgChat.getAllChatsOperations()
                    .stream()
                    .map(Operation::getId)
                    .collect(Collectors.toList());
            ListOperationsEntity entity = new ListOperationsEntity(operations, user.getId());
            cache.putToCache(entity);

            InlineKeyboardMarkup buttons = buttonBuilder.buildButtons(entity, Stage.SETTING_TYPE);

            SendMessage sm = SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .replyMarkup(buttons)
                    .text(textService.getListWelcome())
                    .build();

            send(sm, absSender);
        }
    }

    @Override
    public void handleInline(CallbackQuery query, AbsSender bot) {
        String[] data = query.getData().split("/");

    }


    public enum Stage {
        SETTING_TYPE,
        LIST_MY,
        LIST_ALL;
    }
}
