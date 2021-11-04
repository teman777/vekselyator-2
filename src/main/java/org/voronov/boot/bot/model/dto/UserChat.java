package org.voronov.boot.bot.model.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "UserChatRelation")
public class UserChat {
    @EmbeddedId
    private Key id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChatID")
    private TgChat chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID")
    private TgUser user;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChatID", referencedColumnName = "ChatID")
    @JoinColumn(name = "UFrom", referencedColumnName = "UserID")
    @JoinColumn(name = "UTo", referencedColumnName = "UserID")
    private List<Operation> operations;

    public TgChat getChat() {
        return chat;
    }

    public void setChat(TgChat chat) {
        this.chat = chat;
    }

    public TgUser getUser() {
        return user;
    }

    public void setUser(TgUser user) {
        this.user = user;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    @Embeddable
    public class Key implements Serializable {
        private Long chatId;
        private Long userId;

        public Key() {

        }

        public Key(Long chatId, Long userId) {
            this.chatId = chatId;
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return Objects.equals(chatId, key.chatId) && Objects.equals(userId, key.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(chatId, userId);
        }
    }
}
