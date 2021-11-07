package org.voronov.boot.bot.caches.list;

import org.voronov.boot.bot.caches.core.CachedEntity;

import java.util.ArrayList;
import java.util.List;

public class ListOperationsEntity extends CachedEntity {

    private List<Long> allSortedOperations;

    private List<Long> selectedOperations;

    private Long currentLastOnScreen;

    public ListOperationsEntity() {
        super();
        allSortedOperations = new ArrayList<>();
        selectedOperations = new ArrayList<>();
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
}
