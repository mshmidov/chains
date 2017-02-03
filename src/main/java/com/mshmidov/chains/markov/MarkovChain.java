package com.mshmidov.chains.markov;

import java.util.Optional;

public interface MarkovChain<T>{

    int order();

    T terminalSymbol();

    WeightedRandom<Key<T>> getStartingKeys();

    Optional<WeightedRandom<T>> getNextElements(Key<T> key);

    Iterable<T> asIterable(long maximumElements);
}
