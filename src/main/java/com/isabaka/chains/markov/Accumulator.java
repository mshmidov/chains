package com.isabaka.chains.markov;

import java.util.function.Predicate;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Accumulator<T> extends AbstractAccumulator<T> {

    private final Predicate<Key<T>> startingKeyCriteria;
    private final Multiset<Key<T>> startingKeys = HashMultiset.create();

    Accumulator(int order, Predicate<Key<T>> startingKeyCriteria) {
        super(order);
        this.startingKeyCriteria = startingKeyCriteria;
    }

    public Multiset<Key<T>> getStartingKeys() {
        return startingKeys;
    }

    public Accumulator<T> combine(Accumulator<T> other) {
        super.combine(other);
        other.startingKeys.forEachEntry((key, count) -> this.startingKeys.add(key, count));
        return this;
    }

    protected void put(Key<T> key, T value) {
        super.put(key, value);
        if (startingKeyCriteria.test(key)) {
            startingKeys.add(key);
        }
    }

}
