package com.mshmidov.chains.chain;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;
import com.mshmidov.chains.random.WeightedRandom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public final class MarkovChainBuilder<T> {

    private final int order;
    private final T terminalSymbol;

    private final Map<Key<T>, Multiset<T>> chain = new HashMap<>();

    private final Multiset<Key<T>> startingKeys = HashMultiset.create();

    public MarkovChainBuilder(int order, T terminalSymbol) {
        this.order = order;
        this.terminalSymbol = terminalSymbol;
    }

    public MarkovChainBuilder<T> populate(T[] elements) {

        for (int i = 0; i <= elements.length - order; i++) {
            final Key<T> key = new ArrayKey<>(Arrays.copyOfRange(elements, i, i + order));
            final T value = (i < elements.length - order) ? elements[i + order] : terminalSymbol;
            put(key, value);

            if (i == 0) {
                startingKeys.add(key);
            }
        }

        return this;
    }

    private void put(Key<T> key, T value) {
        checkArgument(key.order() == order);

        chain.computeIfAbsent(key, any -> HashMultiset.create()).add(value);
    }

    public MarkovChain<T> build() {

        final ImmutableMap.Builder<Key<T>, WeightedRandom<T>> builder = ImmutableMap.builder();

        chain.forEach((key, multiset) -> builder.put(key, WeightedRandom.fromMultiset(multiset)));

        return new ImmutableChain<>(order, terminalSymbol, builder.build(), WeightedRandom.fromMultiset(startingKeys));
    }

}
