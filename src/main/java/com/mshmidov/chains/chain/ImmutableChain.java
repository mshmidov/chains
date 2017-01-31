package com.mshmidov.chains.chain;

import com.google.common.collect.ImmutableMap;
import com.mshmidov.chains.random.WeightedRandom;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

final class ImmutableChain<T> implements MarkovChain<T> {

    private final int order;
    private final T terminalSymbol;

    private final ImmutableMap<Key<T>, WeightedRandom<T>> chain;

    private final WeightedRandom<Key<T>> startingKeys;

    ImmutableChain(int order, T terminalSymbol, ImmutableMap<Key<T>, WeightedRandom<T>> chain, WeightedRandom<Key<T>> startingKeys) {
        this.order = order;
        this.terminalSymbol = terminalSymbol;
        this.chain = chain;
        this.startingKeys = startingKeys;
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
        checkArgument(key.order() == order);
        return Optional.ofNullable(chain.get(key));
    }

}
