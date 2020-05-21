package com.isabaka.chains.markov;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.isabaka.chains.util.DisplacementBuffer;

public class AccumulatorOfIterables<T> extends AbstractAccumulator<T> {

    private final Multiset<Key<T>> startingKeys = HashMultiset.create();

    AccumulatorOfIterables(int order) {
        super(order);
    }

    public Multiset<Key<T>> getStartingKeys() {
        return startingKeys;
    }

    public void accumulateIterable(Iterable<T> tokens) {
        final DisplacementBuffer<T> keyBuffer = new DisplacementBuffer<>(order);
        boolean noStartingKey = true;

        for (T token : tokens) {
            accumulate(token);

            if (noStartingKey) {
                keyBuffer.add(token);
            }

            if (keyBuffer.isFull()) {
                startingKeys.add(new ArrayKey<>(keyBuffer.getData()));
                keyBuffer.clear();
                noStartingKey = false;
            }

        }
    }

    public AccumulatorOfIterables<T> combine(AccumulatorOfIterables<T> other) {
        super.combine(other);
        other.startingKeys.forEachEntry((key, count) -> this.startingKeys.add(key, count));
        return this;
    }

}
