package org.voronov.boot.bot.caches.operations;

import org.voronov.boot.bot.caches.core.CachedEntity;

import java.util.ArrayList;
import java.util.List;

public class AddOperationEntity extends CachedEntity {
    private List<Long> to;
    private Long from;
    private Long chat;
    private Double qty;
    private String comment;

    public AddOperationEntity() {
        this.to = new ArrayList<>();
    }

    public void addTo(Long to) {
        this.to.add(to);
    }

    public List<Long> getTo() {
        return to;
    }

    public void setTo(List<Long> to) {
        this.to = to;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getChat() {
        return chat;
    }

    public void setChat(Long chat) {
        this.chat = chat;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
