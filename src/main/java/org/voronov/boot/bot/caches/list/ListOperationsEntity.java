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

    private LinkedHashMap<TgUser, List<Operation>> tgUserListMap = new LinkedHashMap<>();

    private List<TgUser> usersInChat = new ArrayList<>();

    private Long currentSelectedUser = 0L;

    public ListOperationsEntity(Set<Operation> operations, Set<TgUser> usersInChat, Long user) {
        super(user);
        this.usersInChat.addAll(usersInChat);
        operations.forEach(a -> operationMap.put(a.getId(), a));
        buildUserMap();
    }

    private void buildUserMap() {
        List<Operation> byUser = operationMap.values()
                .stream()
                .filter(a -> a.getuTo().getUser().getId().equals(user)
                        || a.getuFrom().getUser().getId().equals(user)).collect(Collectors.toList());
        for (Operation operation : byUser) {
            TgUser tgUser = operation.getuTo().getUser().getId().equals(user)
                    ? operation.getuFrom().getUser()
                    : operation.getuTo().getUser();
            if (tgUserListMap.containsKey(tgUser)) {
                List<Operation> operations = tgUserListMap.get(tgUser);
                operations.add(operation);
            } else {
                List<Operation> operations = new ArrayList<>();
                operations.add(operation);
                tgUserListMap.put(tgUser, operations);
            }
        }
    }

    public boolean isAllSelectedForCurrent() {
        boolean result = true;
        Optional<TgUser> optUser = usersInChat.stream().filter(a -> a.getId().equals(currentSelectedUser)).findFirst();
        if (optUser.isPresent()) {
            TgUser us = optUser.get();
            List<Operation> operations = tgUserListMap.get(us);
            for (Operation operation : operations) {
                if (!selectedOperations.contains(operation.getId())) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public void selectAllForUser(Long userId) {
        Optional<TgUser> optUser = usersInChat.stream().filter(a -> a.getId().equals(userId)).findFirst();
        if (optUser.isPresent()) {
            TgUser us = optUser.get();
            List<Long> allSelected = tgUserListMap.get(us)
                    .stream()
                    .map(Operation::getId)
                    .filter(a -> !selectedOperations.contains(a))
                    .distinct()
                    .collect(Collectors.toList());
            selectedOperations.addAll(allSelected);
        }
    }
    public void deselectAllForUser(Long userId) {
        Optional<TgUser> optUser = usersInChat.stream().filter(a -> a.getId().equals(userId)).findFirst();
        if (optUser.isPresent()) {
            TgUser us = optUser.get();
            List<Long> allSelected = tgUserListMap.get(us)
                    .stream()
                    .map(Operation::getId)
                    .filter(a -> selectedOperations.contains(a))
                    .distinct()
                    .collect(Collectors.toList());
            selectedOperations.removeAll(allSelected);
        }
    }

    public Long getCurrentSelectedUser() {
        return currentSelectedUser;
    }

    public void setCurrentSelectedUser(Long currentSelectedUser) {
        this.currentSelectedUser = currentSelectedUser;
    }

    public List<Operation> getSortedOperationById() {
        return operationMap.values()
                .stream()
                .sorted(Comparator.comparing(Operation::getId))
                .collect(Collectors.toList());
    }

    public LinkedHashMap<TgUser, List<Operation>> getTgUserListMap() {
        return tgUserListMap;
    }

    public LinkedHashMap<TgUser, List<Operation>> getOperationsBySelectedUsers() {
        LinkedHashMap<TgUser, List<Operation>> sorted = new LinkedHashMap<>();
        for (TgUser tgUser : getUsersHaveOperations()) {
            if (selectedUsers.contains(tgUser.getId())) {
                List<Operation> byUser = tgUserListMap.get(tgUser);
                if (!byUser.isEmpty()) {
                    sorted.put(tgUser, byUser);
                }
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
        if (currentSelectedUser == 0) {
            currentSelectedUser = userId;
        }
        selectedUsers.add(userId);
    }

    public void deleteSelectedUser(Long userId) {
        if (selectedUsers.size() == 1) {
            currentSelectedUser = 0L;
        }
        selectedUsers.remove(userId);
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

    public Set<TgUser> getUsersHaveOperations() {
        return tgUserListMap.keySet();
    }

    public List<TgUser> getUnselectedUsers() {
        return tgUserListMap.keySet()
                .stream()
                .filter(a -> !a.getId().equals(currentSelectedUser))
                .collect(Collectors.toList());
    }

    public Optional<TgUser> getSelectedTgUser() {
        return usersInChat.stream().filter(a -> a.getId().equals(currentSelectedUser)).findFirst();
    }

    public void setUsersInChat(List<TgUser> usersInChat) {
        this.usersInChat = usersInChat;
    }

    public enum Type {
        MY, ALL
    }
}
