package org.voronov.boot.bot.services;


import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.voronov.boot.bot.caches.operations.AddOperationEntity;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.TgChat;
import org.voronov.boot.bot.model.dto.TgUser;
import org.voronov.boot.bot.model.dto.UserChat;
import org.voronov.boot.bot.model.repositories.ChatRepository;
import org.voronov.boot.bot.model.repositories.OperationRepository;
import org.voronov.boot.bot.model.repositories.UserChatRepository;
import org.voronov.boot.bot.model.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private UserRepository userRepository;
    private ChatRepository chatRepository;
    private UserChatRepository userChatRepository;
    private OperationRepository operationRepository;

    @Autowired
    private Logger logger;

    @Autowired
    private ChatCache chatCache;

    @Autowired
    public ChatService(UserRepository userRepository, ChatRepository chatRepository, UserChatRepository userChatRepository, OperationRepository operationRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.userChatRepository = userChatRepository;
        this.operationRepository = operationRepository;
    }

    public Optional<TgChat> findChat(Long chatID) {
        return chatRepository.findById(chatID);
    }

    public Optional<TgUser> findUser(Long userID) {
        return userRepository.findById(userID);
    }

    @Transactional
    public void registerUserForChat(Long chatID, Long userID, String userBrief, String chatTitle) {
        Optional<TgChat> chat = findChat(chatID);
        Optional<TgUser> user = findUser(userID);
        TgChat tgChat;
        TgUser tgUser;

        if (user.isPresent()) {
            tgUser = user.get();
            if (!tgUser.getBrief().equals(userBrief)) {
                tgUser.setBrief(userBrief);
                userRepository.save(tgUser);
            }
        } else {
            tgUser = new TgUser();
            tgUser.setId(userID);
            tgUser.setBrief(userBrief);
            userRepository.save(tgUser);
        }

        if (chat.isPresent()) {
            tgChat = chat.get();
            if (!Objects.equal(tgChat.getName(), chatTitle)) {
                tgChat.setName(chatTitle);
                chatRepository.save(tgChat);
            }
        } else {
            tgChat = new TgChat();
            tgChat.setId(chatID);
            tgChat.setName(chatTitle);
            chatRepository.save(tgChat);
        }

        if (!tgChat.isUserExists(userID)) {
            UserChat uc = new UserChat();
            uc.setChat(tgChat);
            uc.setUser(tgUser);
            userChatRepository.save(uc);
        }

        chatCache.updateChat(chatID);
    }

    @Transactional
    public void deleteOperations(List<Long> operations) {
        operationRepository.removeById(operations);
    }

    @Transactional
    public void removeWithSaldo(SaldoEntity entity) {
        operationRepository.removeById(new ArrayList<>(entity.getOperationMap().keySet()));
        operationRepository.saveAll(entity.getUnselectedSaldo());
    }

    public void createOperationFromEntity(AddOperationEntity entity) {
        Optional<TgChat> findedChat = findChat(entity.getChat());
        if (findedChat.isPresent()) {
            TgChat chat = findedChat.get();

            List<Long> tos = entity.getTo();
            List<UserChat> userChatTo = chat.getUsers().stream().filter(a -> tos.contains(a.getUser().getId())).collect(Collectors.toList());

            Optional<UserChat> findedFrom = chat.getUsers().stream().filter(a -> a.getUser().getId().equals(entity.getFrom())).findFirst();

            if (findedFrom.isPresent()) {
                Double qty = entity.getType() == AddOperationEntity.Type.DIVIDE_TO_ALL
                        ? entity.getQty() / userChatTo.size()
                        : entity.getQty();

                userChatTo.remove(findedFrom.get());

                List<Operation> operations = userChatTo.stream().map(a -> {
                    Operation oper = new Operation();
                    oper.setuFrom(findedFrom.get());
                    oper.setuTo(a);
                    oper.setComment(entity.getComment());
                    oper.setQty(qty);
                    oper.setDate(new Date());
                    return oper;
                }).collect(Collectors.toList());

                if (!operations.isEmpty()) {
                    operationRepository.saveAll(operations);
                }
            }
        }
    }
}
