package org.voronov.boot.bot.caches.list;

import org.voronov.boot.bot.caches.core.CachedEntity;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.TgUser;

import java.util.*;
import java.util.stream.Collectors;

public class ListOperationsEntity extends CachedEntity {

    private Type type = Type.MY;
    private List<Long> selectedUsers = new ArrayList<>();
    private List<Long> selectedOperations = new ArrayList<>();

    private Map<Long, Operation> operationMap = new HashMap<>();

    private List<TgUser> usersInChat = new ArrayList<>();

    public ListOperationsEntity(Set<Operation> operations, Set<TgUser> usersInChat, Long user) {
        super(user);
        this.usersInChat.addAll(usersInChat);
        operations.forEach(a -> operationMap.put(a.getId(), a));
    }

    public List<Operation> getSortedOperationById() {
        return operationMap.values()
                .stream()
                .sorted(Comparator.comparing(Operation::getId))
                .collect(Collectors.toList());
    }

    public LinkedHashMap<TgUser, List<Operation>> getOperationsByUsers() {
        LinkedHashMap<TgUser, List<Operation>> sorted = new LinkedHashMap<>();
        List<Operation> allSorted = operationMap.values()
                .stream()
                .filter(a -> a.getuTo().getUser().getId().equals(user)
                        || a.getuFrom().getUser().getId().equals(user)).collect(Collectors.toList());
        for (TgUser tgUser : getAnotherUsersInChat()) {
            List<Operation> byUser = allSorted
                    .stream()
                    .filter(a -> a.getuFrom().getUser().getId().equals(tgUser.getId())
                            || a.getuTo().getUser().getId().equals(tgUser.getId()))
                    .collect(Collectors.toList());
            if (!byUser.isEmpty()) {
                sorted.put(tgUser, byUser);
            }
        }
        return sorted;
    }

    public boolean isUserSelected(TgUser user) {
        return selectedUsers.stream().anyMatch(a -> user.getId().equals(a));
    }

    public void addSelectedOperation(Long operationId) {
        selectedOperations.add(operationId);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void addSelectedUser(Long userId) {
        selectedUsers.add(userId);
    }

    public Type getType() {
        return type;
    }

    public List<Long> getSelectedUsers() {
        return selectedUsers;
    }

    public List<Long> getSelectedOperations() {
        return selectedOperations;
    }

    public Map<Long, Operation> getOperationMap() {
        return operationMap;
    }

    public List<TgUser> getUsersInChat() {
        return usersInChat;
    }

    public List<TgUser> getAnotherUsersInChat() {
        return usersInChat.stream().filter(a -> !a.getId().equals(user)).collect(Collectors.toList());

    }

    public void setUsersInChat(List<TgUser> usersInChat) {
        this.usersInChat = usersInChat;
    }

    public enum Type {
        MY, ALL
    }
}
