package org.voronov.boot.bot.model.dto;

import javax.persistence.*;

@Entity
@Table(name = "UserChatRelation")
public class UserChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChatID")
    private TgChat chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID")
    private TgUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
