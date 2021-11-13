package org.voronov.boot.bot.model.dto;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class TgUser {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "Brief")
    private String brief;

    public TgUser() {

    }

    public TgUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TgUser)) return false;
        TgUser tgUser = (TgUser) o;
        return id.equals(tgUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
