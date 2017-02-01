package com.mshmidov.chains.chain;

import com.google.common.collect.ImmutableMap;
import com.mshmidov.chains.random.WeightedRandom;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

final class ImmutableChain<T> implements MarkovChain<T> {

    private final int order;
    private final T terminalSymbol;

    private final ImmutableMap<Key<T>, WeightedRandom<T>> chain;

    private final WeightedRandom<Key<T>> startingKeys;

    ImmutableChain(int order, T terminalSymbol, ImmutableMap<Key<T>, WeightedRandom<T>> chain, WeightedRandom<Key<T>> startingKeys) {
        this.order = order;
        this.terminalSymbol = requireNonNull(terminalSymbol);
        this.chain = requireNonNull(chain);
        this.startingKeys = requireNonNull(startingKeys);
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public T terminalSymbol() {
        return terminalSymbol;
    }

    @Override
    public WeightedRandom<Key<T>> getStartingKeys() {
        return startingKeys;
    }

    @Override
    public Optional<WeightedRandom<T>> getNextElements(Key<T> key) {
        return Optional.ofNullable(chain.get(key));
    }

}
