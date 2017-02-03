package com.mshmidov.chains.markov;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;
import com.mshmidov.chains.util.DisplacementBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

public final class MarkovChainBuilder<T> {

    private final int order;
    private final T terminalSymbol;

    private final Map<Key<T>, Multiset<T>> chain = new HashMap<>();

    private final Multiset<Key<T>> startingKeys = HashMultiset.create();

    public static <T> MarkovChainBuilder<T> newInstance(int order, T terminalSymbol) {
        return new MarkovChainBuilder<>(order, terminalSymbol);
    }

    private MarkovChainBuilder(int order, T terminalSymbol) {
        this.order = order;
        this.terminalSymbol = terminalSymbol;
    }

    public MarkovChainBuilder<T> populate(Iterable<T> elements) {

        final DisplacementBuffer<T> keyBuffer = new DisplacementBuffer<>(order);

        boolean keyBufferWasEmpty = true;

        for (T element : elements) {
            keyBufferWasEmpty = processElement(keyBuffer, element, keyBufferWasEmpty);
        }

        processElement(keyBuffer, terminalSymbol, keyBufferWasEmpty);

        return this;
    }

    public MarkovChain<T> build() {

        final ImmutableMap.Builder<Key<T>, WeightedRandom<T>> builder = ImmutableMap.builder();

        chain.forEach((key, multiset) -> builder.put(key, SortedMapWeightedRandom.fromMultiset(multiset)));

        return new ImmutableChain<>(order, terminalSymbol, builder.build(), SortedMapWeightedRandom.fromMultiset(startingKeys));
    }

    private boolean processElement(DisplacementBuffer<T> keyBuffer, T element, boolean keyBufferWasEmpty) {
        final boolean terminalElement = Objects.equals(element, terminalSymbol);
        final boolean keyBufferFull = keyBuffer.getDataSize() == order;

        if (keyBufferFull) {
            final ArrayKey<T> key = new ArrayKey<>(keyBuffer.getData());
            put(key, element);

            if (keyBufferWasEmpty) {
                startingKeys.add(key);
            }
        }

        if (terminalElement) {
            keyBuffer.clear();
        } else {
            keyBuffer.add(element);
        }

        return !keyBufferFull;
    }

    private void put(Key<T> key, T value) {
        checkArgument(key.order() == order);

        chain.computeIfAbsent(key, any -> HashMultiset.create()).add(value);
    }

}
