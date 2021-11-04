package org.voronov.boot.bot.model.dto;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Operations")
public class Operation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Qty")
    private Double qty;

    @ManyToOne
    @JoinColumn(name = "UTo", referencedColumnName = "ID")
    private UserChat uTo;

    @ManyToOne
    @JoinColumn(name = "UFrom", referencedColumnName = "ID")
    private UserChat uFrom;


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

    public UserChat getuTo() {
        return uTo;
    }

    public void setuTo(UserChat uTo) {
        this.uTo = uTo;
    }

    public UserChat getuFrom() {
        return uFrom;
    }

    public void setuFrom(UserChat uFrom) {
        this.uFrom = uFrom;
    }
}
