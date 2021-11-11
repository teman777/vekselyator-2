package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.voronov.boot.bot.caches.list.ListOperationsCache;
import org.voronov.boot.bot.caches.list.ListOperationsEntity;
import org.voronov.boot.bot.commands.core.AbstractCommand;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.TgChat;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.MessageTextService;
import org.voronov.boot.bot.services.buttons.ListButtonBuilderService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ListCommand extends AbstractCommand {

    public static final List<String> INLINE_COMMANDS = Arrays.asList("cancelList", "nextList", "prevList", "my", "all", "select", "deleteOperations");

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
            ListOperationsEntity entity = new ListOperationsEntity(operations);
            entity.setAllSortedOperations(allOperations);
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

    public void handleInline(CallbackQuery query, AbsSender bot) {

    }


    public enum Type {
        MY, ALL;
    }

    public enum Stage {
        SETTING_TYPE,
        LIST
    }
}
