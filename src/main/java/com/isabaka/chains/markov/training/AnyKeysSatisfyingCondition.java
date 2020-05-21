package com.isabaka.chains.markov.training;

import java.util.Collection;
import java.util.function.Predicate;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.isabaka.chains.markov.ArrayKey;
import com.isabaka.chains.markov.Key;
import com.isabaka.chains.util.DisplacementBuffer;

class AnyKeysSatisfyingCondition<T> implements StaringKeysTraining<T> {

    private final Predicate<Key<T>> condition;

    private final DisplacementBuffer<T> buffer;
    private final Multiset<Key<T>> startingKeys = HashMultiset.create();

    public AnyKeysSatisfyingCondition(int order, Predicate<Key<T>> condition) {
        this.condition = condition;
        this.buffer = new DisplacementBuffer<>(order);
    }

    @Override
    public void acceptElement(T element) {
        if (buffer.isFull()) {
            final Key<T> key = new ArrayKey<>(buffer.getData());
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
    public Multiset<Key<T>> getStartingKeys() {
        return startingKeys;
    }

}
