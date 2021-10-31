package org.voronov.boot.bot.model.dto;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Chats")
public class TgChat {
    @Id
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ChatID")
    private List<UserChat> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserChat> getUsers() {
        return users;
    }

    public void setUsers(List<UserChat> users) {
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
