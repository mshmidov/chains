package com.mshmidov.chains.chain;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;
import com.mshmidov.chains.random.WeightedRandom;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

        final DisplacementBuffer<T> keyBuffer = new DisplacementBuffer<>(order);

        boolean keyBufferWasEmpty = true;

        for (T element : elements) {
            keyBufferWasEmpty = processElement(keyBuffer, element, keyBufferWasEmpty);
        }

        if (!Objects.equals(elements[elements.length - 1], terminalSymbol)) {
            processElement(keyBuffer, terminalSymbol, keyBufferWasEmpty);
        }

        return this;
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

    public MarkovChain<T> build() {

        final ImmutableMap.Builder<Key<T>, WeightedRandom<T>> builder = ImmutableMap.builder();

        chain.forEach((key, multiset) -> builder.put(key, WeightedRandom.fromMultiset(multiset)));

        return new ImmutableChain<>(order, terminalSymbol, builder.build(), WeightedRandom.fromMultiset(startingKeys));
    }

}
