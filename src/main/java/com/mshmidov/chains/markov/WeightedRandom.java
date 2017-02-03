package com.mshmidov.chains.markov;

import com.google.common.collect.ImmutableSortedMap;

public interface WeightedRandom<T> {

    T choose(double random);

    ImmutableSortedMap<Double, T> values();
}
