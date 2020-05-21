package com.isabaka.chains.markov.training;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.isabaka.chains.markov.ArrayKey;
import com.isabaka.chains.markov.Key;

import static com.google.common.base.Preconditions.checkArgument;

public class Training<T> {

    private final int order;
    private final DisplacementBuffer<T> buffer;

    private Map<Key<T>, Probabilities<T>> trainingData;
    private final StaringKeysTrainingData<T> staringKeysTraining;

    public Training(int order, StartingKeysExtraction<T> startingKeysExtraction) {
        this.order = order;
        this.buffer = new DisplacementBuffer<>(order);
        this.trainingData = new HashMap<>();
        this.staringKeysTraining = startingKeysExtraction.forOrder(order);
    }

    public int getOrder() {
        return order;
    }

    public Map<Key<T>, Probabilities<T>> getTrainingData() {
        return trainingData;
    }

    public Probabilities<Key<T>> getStaringKeys() {
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

    private void addToChain(Key<T> key, T element) {
        checkArgument(key.order() == order);
        trainingData.computeIfAbsent(key, k -> new Probabilities<>()).add(element);
    }
}
