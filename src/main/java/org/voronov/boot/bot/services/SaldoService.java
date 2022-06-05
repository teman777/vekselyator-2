package org.voronov.boot.bot.services;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.voronov.boot.bot.caches.saldo.SaldoEntity;
import org.voronov.boot.bot.model.dto.Operation;
import org.voronov.boot.bot.model.dto.UserChat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class SaldoService {

    public void optimize(SaldoEntity entity) {
        Set<Operation> saldo = optimizeEntity(entity);
        entity.setSaldoAll(saldo);
    }

    public Set<Operation> optimizeEntity(Set<Operation> operations) {
        Map<UserChat, Double> userAndBalance = buildUserMap(operations);
        Set<Operation> newOperations = new LinkedHashSet<>();

        boolean saldoIsReady = false;

        while (!saldoIsReady) {
            UserChat maxUser = find(userAndBalance, Type.MAX);
            UserChat minUser = find(userAndBalance, Type.MIN);

            Double max = userAndBalance.get(maxUser);
            Double min = userAndBalance.get(minUser);

            if (max == null || min == null) {
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

            newOperations.add(saldo);

            if (userAndBalance.keySet().isEmpty()) {
                saldoIsReady = true;
            }
        }

        return newOperations;
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

    public Set<Operation> considerWhatShouldBeDeleted(SaldoEntity entity) {
        List<Operation> selectedSaldo = entity.getSaldoAll().stream()
                .filter(a -> entity.getSelectedSaldo().contains(a.getId()))
                .toList();

        Map<Key, Double> dummySaldo = buildDummySaldoMap(entity.getOperationMap().values());

        DefaultDirectedWeightedGraph<UserChat, Double> graph = buildGraph(dummySaldo);

        return null;
    }


    private DefaultDirectedWeightedGraph<UserChat, Double> buildGraph(Map<Key, Double> dummySaldo) {
        DefaultDirectedWeightedGraph<UserChat, Double> graph = new DefaultDirectedWeightedGraph<>(Double.class);
        dummySaldo.forEach((key, value) -> {
            UserChat first = key.getFirst();
            UserChat second = key.getSecond();
            initVertex(graph, key);
            graph.addEdge(first, second, value);
        });

        return graph;
    }

    private void initVertex(DefaultDirectedWeightedGraph<UserChat, Double> graph, Key key) {
        UserChat first = key.getFirst();
        UserChat second = key.getSecond();

        if (!graph.containsVertex(first)) {
            graph.addVertex(first);
        }

        if (!graph.containsVertex(second)) {
            graph.addVertex(second);
        }
    }


    private Map<Key, Double> buildDummySaldoMap(Collection<Operation> operations) {
        Map<Key, Double> result = new HashMap<>();
        for (Operation operation : operations) {
            UserChat first = operation.getuFrom();
            UserChat second = operation.getuTo();

            Double value = operation.getQty();

            Key key = new Key(first, second);

            if (result.containsKey(key.inverse())) {
                result.computeIfPresent(key.inverse(), (key1, aDouble) -> aDouble - value);
            } else if (result.containsKey(key)) {
                result.computeIfPresent(key, (key1, aDouble) -> aDouble + value);
            } else {
                result.put(key, value);
            }
        }
        return result;
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
                .map(Map.Entry::getKey).toList();

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
        MIN
    }

    private static class Key {
        UserChat first;
        UserChat second;

        public Key(@NonNull UserChat first, @NonNull UserChat second) {
            this.first = first;
            this.second = second;
        }

        public UserChat getFirst() {
            return first;
        }

        public UserChat getSecond() {
            return second;
        }

        public Key inverse() {
            return new Key(second, first);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return first.equals(key.first) && second.equals(key.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
}
