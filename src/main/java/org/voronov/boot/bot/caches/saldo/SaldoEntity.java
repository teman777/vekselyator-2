package org.voronov.boot.bot.caches.saldo;

import org.voronov.boot.bot.caches.core.CachedEntity;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.TgUser;

import java.util.*;
import java.util.stream.Collectors;

public class SaldoEntity extends CachedEntity {

    private List<Operation> saldoForUser = new ArrayList<>();

    private List<Operation> saldoAll = new ArrayList<>();

    private List<Long> selectedSaldo = new ArrayList<>();

    private Map<Long, Operation> operationMap = new LinkedHashMap<>();

    private List<TgUser> users = new ArrayList<>();

    public SaldoEntity(Set<Operation> operations, Set<TgUser> usersInChat, Long user) {
        super(user);
        users.addAll(usersInChat);
        operations.forEach(a -> operationMap.put(a.getId(), a));
    }

    public List<Operation> getUnselectedSaldo() {
        List<Operation> ops = saldoAll.stream().filter(a -> !selectedSaldo.contains(a.getId())).collect(Collectors.toList());
        ops.forEach(a -> a.setId(null));
        return ops;
    }

    public List<Long> getSelectedSaldo() {
        return selectedSaldo;
    }

    public List<Operation> getSaldoForUser() {
        return saldoForUser;
    }

    public void setSaldoForUser(List<Operation> saldoForUser) {
        this.saldoForUser = saldoForUser;
    }

    public void addSelectedSaldo(Long id) {
        selectedSaldo.add(id);
    }

    public void removeSelectedSaldo(Long id) {
        selectedSaldo.remove(id);
    }

    public List<Operation> getSaldoAll() {
        return saldoAll;
    }

    public void setSaldoAll(Collection<Operation> saldoAll) {
        this.saldoAll.clear();
        this.saldoAll.addAll(saldoAll);
        buildSaldoForUser();
    }

    private void buildSaldoForUser() {
        saldoForUser.clear();
        saldoForUser.addAll(saldoAll.stream()
                .filter(a -> a.getuTo().getUser().getId().equals(user)
                        || a.getuFrom().getUser().getId().equals(user))
                .collect(Collectors.toList()));

    }

    public Map<Long, Operation> getOperationMap() {
        return operationMap;
    }
}
