package org.voronov.boot.bot.model.dto;


import javax.persistence.*;

@Entity
@Table(name = "Operations")
public class Operation {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UFrom", referencedColumnName = "ID")
    private TgUser userFrom;

    @ManyToOne
    @JoinColumn(name = "UTo", referencedColumnName = "ID")
    private TgUser userTo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TgUser getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(TgUser userFrom) {
        this.userFrom = userFrom;
    }

    public TgUser getUserTo() {
        return userTo;
    }

    public void setUserTo(TgUser userTo) {
        this.userTo = userTo;
    }
}
