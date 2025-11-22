package com.fa.util.listeners;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractMulticaster<T> {

    private final Set<T> listeners;

    public AbstractMulticaster() {
        this.listeners = new HashSet<>();
    }

    public boolean addListener(T listener) {
        return listeners.add(listener);
    }

    public boolean removeListener(T listener) {
        return listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public Set<T> getListeners() {
        return this.listeners;
    }

    public int size() {
        return this.listeners.size();
    }

}
