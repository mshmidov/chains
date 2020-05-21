package com.isabaka.chains.markov;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.isabaka.chains.util.DisplacementBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class AbstractAccumulator<T> {

    protected final int order;
    protected final DisplacementBuffer<T> buffer;
    protected final Map<Key<T>, Multiset<T>> chain = new HashMap<>();

    protected AbstractAccumulator(int order) {
        this.order = order;
        this.buffer = new DisplacementBuffer<>(order);
    }

    public void accumulate(T token) {
        if (buffer.isFull()) {
            final Key<T> key = new ArrayKey<>(buffer.getData());
            put(key, token);
        }

        buffer.add(token);
    }

    public abstract Multiset<Key<T>> getStartingKeys();

    protected void combine(AbstractAccumulator<T> other) {
        if (other.order != this.order) {
            throw new IllegalArgumentException("Unable to combine Accumulators of different order");
        }

        other.chain.forEach((key, tokens) -> tokens.forEachEntry((token, count) -> put(key, token, count)));
    }

    protected void put(Key<T> key, T value) {
        checkArgument(key.order() == order);

        chain.computeIfAbsent(key, any -> HashMultiset.create()).add(value);

    }

    protected void put(Key<T> key, T value, int count) {
        checkArgument(key.order() == order);

        chain.computeIfAbsent(key, any -> HashMultiset.create()).add(value, count);
    }

}
