package org.voronov.boot.bot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.voronov.boot.bot.caches.saldo.SaldoCache;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.TgChat;
import org.voronov.boot.bot.model.dto.TgUser;
import org.voronov.boot.bot.services.ChatService;
import org.voronov.boot.bot.services.SaldoService;
import org.voronov.boot.bot.services.buttons.SaldoButtonBuilderService;
import org.voronov.boot.core.AbstractCommand;

import java.util.Optional;
import java.util.Set;

@Component
public class SaldoCommand extends AbstractCommand {

    @Autowired
    private SaldoService saldoService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private SaldoCache cache;

    @Autowired
    private SaldoButtonBuilderService buttonBuilder;

    public SaldoCommand() {
        super("saldo", "Взаимовычет");
    }

    @Override
    protected void __execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Optional<TgChat> findedChat = chatService.findChat(chat.getId());
        if (findedChat.isPresent()) {
            TgChat tgChat = findedChat.get();
            Set<Operation> operations = tgChat.getAllChatsOperations();
            Set<TgUser> users = tgChat.getAllUsersInChat();
            SaldoEntity entity = new SaldoEntity(operations, users, user.getId());
            saldoService.optimize(entity);
            cache.putToCache(entity);

            InlineKeyboardMarkup markup = buttonBuilder.buildButtons(entity, Stage.SHOW);

            SendMessage sm = SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .replyMarkup(markup)
                    .text("Взаимный расчет")
                    .build();

            send(sm, absSender);
        }
    }

    public enum Stage {
        SHOW, CONFIRM, YES;
    }
}
