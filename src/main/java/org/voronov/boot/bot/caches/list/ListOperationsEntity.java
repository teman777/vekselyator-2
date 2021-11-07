package org.voronov.boot.bot.caches.list;

import org.voronov.boot.bot.caches.core.CachedEntity;
import org.voronov.boot.bot.model.dto.Operation;

import java.util.*;

public class ListOperationsEntity extends CachedEntity {

    private List<Long> allSortedOperations;

    private List<Long> selectedOperations;

    private Map<Long, Operation> operationMap;

    private Long currentLastOnScreen;

    public ListOperationsEntity(Set<Operation> operations) {
        super();
        allSortedOperations = new ArrayList<>();
        selectedOperations = new ArrayList<>();
        operationMap = new HashMap<>();

        if (operations != null) {
            operations.forEach(operation -> operationMap.put(operation.getId(), operation));
        }
        allSortedOperations.addAll(operationMap.keySet());
    }

    public List<Long> getAllSortedOperations() {
        return allSortedOperations;
    }

    public void setAllSortedOperations(List<Long> allSortedOperations) {
        this.allSortedOperations = allSortedOperations;
    }

    public List<Long> getSelectedOperations() {
        return selectedOperations;
    }

    public void addToSelectedOperations(Long operationId) {
        selectedOperations.add(operationId);
    }

    public void addAllToSelectedOperations(List<Long> operationIds) {
        selectedOperations.addAll(operationIds);
    }

    public void setSelectedOperations(List<Long> selectedOperations) {
        this.selectedOperations = selectedOperations;
    }

    public Long getCurrentLastOnScreen() {
        return currentLastOnScreen;
    }

    public void setCurrentLastOnScreen(Long currentLastOnScreen) {
        this.currentLastOnScreen = currentLastOnScreen;
    }

    public Map<Long, Operation> getOperationMap() {
        return operationMap;
    }

    public void setOperationMap(Map<Long, Operation> operationMap) {
        this.operationMap = operationMap;
    }
}
