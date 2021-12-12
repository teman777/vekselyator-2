package org.voronov.boot.bot.caches.operations;

import org.voronov.boot.bot.caches.core.CachedEntity;
import org.voronov.boot.bot.model.dto.TgUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddOperationEntity extends CachedEntity {
    private List<Long> to;
    private long from;
    private long chat;
    private double qty;
    private String comment;
    private Type type = Type.FOR_ONE;
    private List<TgUser> users = new ArrayList<>();

    public AddOperationEntity(Long from, Collection<TgUser> users) {
        super(from);
        this.to = new ArrayList<>();
        this.users.addAll(users);
    }

    public void addTo(Long to) {
        this.to.add(to);
    }

    public void deleteFromTo(Long to) {
        this.to.remove(to);
    }

    public List<Long> getTo() {
        return to;
    }

    public void setTo(List<Long> to) {
        this.to = to;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<TgUser> getUsers() {
        return users;
    }

    public TgUser getFromUser() {
        Optional<TgUser> tgUser = users.stream()
                .filter(a -> a.getId().equals(from))
                .findFirst();

        return tgUser.orElse(null);
    }

    public List<TgUser> getToUsers() {
        return users.stream()
                .filter(a -> to.contains(a.getId()))
                .collect(Collectors.toList());
    }

    public List<TgUser> getToUsersWithoutSelf() {
        return users.stream()
                .filter(a -> to.contains(a.getId()) && !a.getId().equals(from))
                .collect(Collectors.toList());
    }

    public Double getQtyForOne() {
        return type == Type.DIVIDE_TO_ALL
                ? qty / to.size()
                : qty;
    }

    public void setUsers(List<TgUser> users) {
        this.users = users;
    }

    public enum Type {
        DIVIDE_TO_ALL(0),
        NOT_DIVIDE(1),
        FOR_ONE(2);

        private final int code;

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
