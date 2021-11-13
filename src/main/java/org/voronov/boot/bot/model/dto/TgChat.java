package org.voronov.boot.bot.model.dto;


import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Chats")
public class TgChat {
    @Id
    @Column(name = "ID")
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChatID")
    private Set<UserChat> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UserChat> getUsers() {
        return users;
    }

    public void setUsers(Set<UserChat> users) {
        this.users = users;
    }

    public boolean isUserExists(Long userId) {
        if (users != null) {
            return users.stream().anyMatch(user -> user.getUser().getId().equals(userId));
        } else {
            return false;
        }
    }

    public Set<Operation> getAllChatsOperations() {
        if (users != null) {
            return users
                    .stream()
                    .flatMap(a -> a.getAllOperations().stream())
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    public Set<TgUser> getAllUsersInChat() {
        if (users != null) {
            return users.stream().map(UserChat::getUser).collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    public void addUser(UserChat user) {

    }
}
