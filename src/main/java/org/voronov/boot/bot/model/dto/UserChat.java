package org.voronov.boot.bot.model.dto;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "UserChatRelation")
public class UserChat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChatID", referencedColumnName = "ID")
    private TgChat chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID", referencedColumnName = "ID")
    private TgUser user;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "UTo", referencedColumnName = "ID")
    @Where(clause = "IsDeleted = 0")
    private Set<Operation> takedOperations;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "UFrom", referencedColumnName = "ID")
    @Where(clause = "IsDeleted = 0")
    private Set<Operation> givedOperations;

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

    public Set<Operation> getTakedOperations() {
        return takedOperations;
    }

    public void setTakedOperations(Set<Operation> takedOperations) {
        this.takedOperations = takedOperations;
    }

    public Set<Operation> getGivedOperations() {
        return givedOperations;
    }

    public void setGivedOperations(Set<Operation> givedOperations) {
        this.givedOperations = givedOperations;
    }

    public Set<Operation> getAllOperations() {
        Set<Operation> operations = new HashSet<>();
        if (CollectionUtils.isNotEmpty(takedOperations)) {
            operations.addAll(takedOperations);
        }

        if (CollectionUtils.isNotEmpty(givedOperations)) {
            operations.addAll(givedOperations);
        }
        return operations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
