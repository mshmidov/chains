package com.isabaka.chains.markov.training;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.isabaka.chains.markov.ArrayKey;
import com.isabaka.chains.markov.Key;
import com.isabaka.chains.util.DisplacementBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class Training<T> {

    private final int order;
    private final DisplacementBuffer<T> buffer;

    private final StaringKeysTraining<T> staringKeysTraining;

    private final Map<Key<T>, Multiset<T>> chain = new HashMap<>();

    public Training(int order, StartingKeysExtraction<T> startingKeysExtraction) {
        this.order = order;
        this.buffer = new DisplacementBuffer<>(order);
        this.staringKeysTraining = startingKeysExtraction.forOrder(order);
    }

    public int getOrder() {
        return order;
    }

    public Map<Key<T>, Multiset<T>> getChain() {
        return chain;
    }

    public Multiset<Key<T>> getStartingKeys() {
        return staringKeysTraining.getStartingKeys();
    }

    public void acceptElement(T element) {
        addToBuffer(element);
        staringKeysTraining.acceptElement(element);
    }

    public void acceptCorpus(Collection<T> elements) {
        elements.forEach(this::addToBuffer);
        staringKeysTraining.acceptCorpus(elements);
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
