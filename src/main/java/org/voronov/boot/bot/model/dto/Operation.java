package org.voronov.boot.bot.model.dto;


import javax.persistence.*;

@Entity
@Table(name = "Operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Qty")
    private Double qty;

    @ManyToOne
    @JoinColumn(name = "ChatID", referencedColumnName = "ChatID", insertable = false, updatable = false)
    @JoinColumn(name = "UTo", referencedColumnName = "UserID", insertable = false, updatable = false)
    private UserChat userChatTo;

    @ManyToOne
    @JoinColumn(name = "ChatID", referencedColumnName = "ChatID", insertable = false, updatable = false)
    @JoinColumn(name = "UFrom", referencedColumnName = "UserID", insertable = false, updatable = false)
    private UserChat userChatFrom;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public UserChat getUserChatTo() {
        return userChatTo;
    }

    public void setUserChatTo(UserChat userChatTo) {
        this.userChatTo = userChatTo;
    }

    public UserChat getUserChatFrom() {
        return userChatFrom;
    }

    public void setUserChatFrom(UserChat userChatFrom) {
        this.userChatFrom = userChatFrom;
    }
}
