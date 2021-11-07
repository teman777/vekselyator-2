package org.voronov.boot.bot.model.dto;


import javax.persistence.*;
import java.util.Set;

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

    public void addUser(UserChat user) {

    }
}
