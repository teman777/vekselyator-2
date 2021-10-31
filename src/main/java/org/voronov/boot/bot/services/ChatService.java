package org.voronov.boot.bot.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.voronov.boot.bot.model.dto.TgChat;
import org.voronov.boot.bot.model.dto.TgUser;
import org.voronov.boot.bot.model.dto.UserChat;
import org.voronov.boot.bot.model.repositories.ChatRepository;
import org.voronov.boot.bot.model.repositories.UserChatRepository;
import org.voronov.boot.bot.model.repositories.UserRepository;

import java.util.Optional;

@Service
public class ChatService {
    private UserRepository userRepository;
    private ChatRepository chatRepository;
    private UserChatRepository userChatRepository;

    @Autowired
    public ChatService(UserRepository userRepository, ChatRepository chatRepository, UserChatRepository userChatRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.userChatRepository = userChatRepository;
    }

    public Optional<TgChat> findChat(Long chatID) {
        return chatRepository.findById(chatID);
    }

    public Optional<TgUser> findUser(Long userID) {
        return userRepository.findById(userID);
    }

    public void registerUserForChat(Long chatID, Long userID, String userBrief) {
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
        } else {
            tgChat = new TgChat();
            tgChat.setId(chatID);
        }

        if (!tgChat.isUserExists(userID)) {
            UserChat uc = new UserChat();
            uc.setChat(tgChat);
            uc.setUser(tgUser);
            tgChat.addUser(uc);
            chatRepository.save(tgChat);
            userChatRepository.save(uc);
        }
    }
}
