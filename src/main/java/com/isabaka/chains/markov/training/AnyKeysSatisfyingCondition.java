package com.isabaka.chains.markov.training;

import java.util.Collection;
import java.util.function.Predicate;

import com.isabaka.chains.markov.Key;

class AnyKeysSatisfyingCondition<T> implements StaringKeysTrainingData<T> {

    private final Predicate<Key<T>> condition;

    private final DisplacementBuffer<T> buffer;
    private final Probabilities<Key<T>> startingKeys = new Probabilities<>();

    public AnyKeysSatisfyingCondition(int order, Predicate<Key<T>> condition) {
        this.condition = condition;
        this.buffer = new DisplacementBuffer<>(order);
    }

    @Override
    public void acceptElement(T element) {
        if (buffer.isFull()) {
            final Key<T> key = new Key<T>(buffer.getData());
            if (condition.test(key)) {
                startingKeys.add(key);
            }
        }

        buffer.add(element);
    }

    @Override
    public void acceptCorpus(Collection<T> elements) {
        elements.forEach(this::acceptElement);
    }

    @Override
    public Probabilities<Key<T>> getStartingKeys() {
        return startingKeys;
    }

}
