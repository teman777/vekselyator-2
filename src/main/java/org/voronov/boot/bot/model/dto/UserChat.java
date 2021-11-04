package org.voronov.boot.bot.model.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "UserChatRelation")
public class UserChat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChatID")
    private TgChat chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID")
    private TgUser user;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "UTo", referencedColumnName = "ID")
    @JoinColumn(name = "UFrom", referencedColumnName = "ID")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
