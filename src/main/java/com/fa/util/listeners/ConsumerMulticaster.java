package com.fa.util.listeners;

import java.util.function.Consumer;

public class ConsumerMulticaster<T> extends AbstractMulticaster<Consumer<T>> {

    public void alert(T input) {
        getListeners().forEach(listener -> listener.accept(input));
    }

}
