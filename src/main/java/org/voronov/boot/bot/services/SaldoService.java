package org.voronov.boot.bot.services;

import org.springframework.stereotype.Service;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.UserChat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaldoService {

    public void optimize(SaldoEntity entity) {
        Set<Operation> saldo = optimizeEntity(entity);
        entity.setSaldoAll(saldo);
    }

    public Set<Operation> optimizeEntity(SaldoEntity entity) {
        Collection<Operation> operationList = entity.getOperationMap().values();
        Map<UserChat, Double> userAndBalance = buildUserMap(operationList);
        Set<Operation> newOperations = new LinkedHashSet<>();
        long id = 1;

        boolean saldoIsReady = false;

        while (!saldoIsReady) {
            UserChat maxUser = find(userAndBalance, Type.MAX);
            UserChat minUser = find(userAndBalance, Type.MIN);

            Double max = userAndBalance.get(maxUser);
            Double min = userAndBalance.get(minUser);

            if (max == null || min == null) {
                entity.setErrorBalance(max == null ? min : max);
                break;
            }

            Operation saldo = new Operation();
            saldo.setuFrom(maxUser);
            saldo.setuTo(minUser);
            saldo.setDate(new Date());
            if (max + min > 0) {
                saldo.setQty(Math.abs(min));
                Double balance = userAndBalance.get(maxUser);
                balance -= Math.abs(min);
                userAndBalance.put(maxUser, balance);

                userAndBalance.remove(minUser);

            } else if (max + min <= 0) {
                saldo.setQty(Math.abs(max));
                Double balance = userAndBalance.get(minUser);
                balance += Math.abs(max);
                userAndBalance.put(minUser, balance);

                userAndBalance.remove(maxUser);

                if (max + min == 0) {
                    userAndBalance.remove(minUser);
                }
            }
            saldo.setId(id);
            id++;
            newOperations.add(saldo);

            if (userAndBalance.keySet().isEmpty()) {
                saldoIsReady = true;
            }
        }

        return newOperations;
    }

    private static UserChat find(Map<UserChat, Double> map, Type type) {
        Double findedQty = 0d;
        UserChat findedUser = null;

        for (UserChat user : map.keySet()) {
            Double qty = map.get(user);
            if ((qty < findedQty && type == Type.MIN)
                    || (qty > findedQty && type == Type.MAX)) {
                findedQty = qty;
                findedUser = user;
            }
        }
        return findedUser;
    }

    private static Map<UserChat, Double> buildUserMap(Collection<Operation> operations) {
        Map<UserChat, Double> userAndBalance = new LinkedHashMap<>();
        operations.forEach(operation -> {
            UserChat from = operation.getuFrom();
            UserChat to = operation.getuTo();

            Double qtyFrom = userAndBalance.get(from);
            Double qtyTo = userAndBalance.get(to);

            if (qtyFrom == null) {
                qtyFrom = Math.abs(operation.getQty());
            } else {
                qtyFrom += operation.getQty();
            }

            if (qtyTo == null) {
                qtyTo = -1 * Math.abs(operation.getQty());
            } else {
                qtyTo -= operation.getQty();

            }

            userAndBalance.put(from, qtyFrom);
            userAndBalance.put(to, qtyTo);

        });
        List<UserChat> needToRemove = userAndBalance.entrySet()
                .stream()
                .filter(a -> a.getValue() == null || a.getValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (UserChat uc : needToRemove) {
            userAndBalance.remove(uc);
        }

        userAndBalance.entrySet().forEach(entry -> {
            Double value = entry.getValue();
            BigDecimal val = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
            Double newValue = val.doubleValue();
            entry.setValue(newValue);
        });

        return userAndBalance;
    }

    private enum Type {
        MAX,
        MIN;
    }
}
