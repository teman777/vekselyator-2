package org.voronov.boot.bot.model.dto;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Users")
public class TgUser {
    @Id
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
}
