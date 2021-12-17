package org.voronov.boot.bot.model.dto;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Operations")
public class Operation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Qty")
    private Double qty;

    @ManyToOne
    @JoinColumn(name = "UTo", referencedColumnName = "ID")
    private UserChat uTo;

    @ManyToOne
    @JoinColumn(name = "UFrom", referencedColumnName = "ID")
    private UserChat uFrom;

    @Column(name = "Comment")
    private String comment;

    @Column(name = "Date")
    private Date date;

    @Column(name = "IsDeleted")
    private Boolean isDeleted = false;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
