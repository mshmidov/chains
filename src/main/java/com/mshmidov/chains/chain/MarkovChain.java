package com.mshmidov.chains.chain;

import com.google.common.collect.ImmutableMultiset;
import com.mshmidov.chains.random.WeightedRandom;

import java.util.Optional;

public interface MarkovChain<T> {

    int order();

    T terminalSymbol();

    WeightedRandom<Key<T>> getStartingKeys();

    Optional<WeightedRandom<T>> getNextElements(Key<T> key);
}
