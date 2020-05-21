package com.isabaka.chains.markov;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.isabaka.chains.util.DisplacementBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class Training<T> {

    private final int order;
    private final DisplacementBuffer<T> buffer;

    private final StartingKeysStrategy<T> startingKeysStrategy;

    private final Map<Key<T>, Multiset<T>> chain = new HashMap<>();

    public Training(int order, StartingKeysStrategy<T> startingKeysStrategy) {
        this.order = order;
        this.buffer = new DisplacementBuffer<>(order);
        this.startingKeysStrategy = startingKeysStrategy;
    }

    public int getOrder() {
        return order;
    }

    public Map<Key<T>, Multiset<T>> getChain() {
        return chain;
    }

    public Multiset<Key<T>> getStartingKeys() {
        return startingKeysStrategy.getStartingKeys();
    }

    public void acceptElement(T element) {
        addToBuffer(element);
        startingKeysStrategy.acceptElement(element);
    }

    public void acceptCorpus(Collection<T> elements) {
        elements.forEach(this::addToBuffer);
        startingKeysStrategy.acceptCorpus(elements);
    }

    private void addToBuffer(T element) {
        Objects.requireNonNull(element);

        if (buffer.isFull()) {
            final Key<T> key = new ArrayKey<>(buffer.getData());
            addToChain(key, element);
        }

        buffer.add(element);
    }

    private void addToChain(Key<T> key, T value) {
        checkArgument(key.order() == order);

        chain.computeIfAbsent(key, any -> HashMultiset.create()).add(value);
    }
}
