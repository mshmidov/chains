package com.isabaka.chains.markov.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

public final class Probabilities<T> {

    private final Multiset<T> elements = HashMultiset.create();

    public Probabilities() {
    }

    public void add(T value) {
        elements.add(Objects.requireNonNull(value));
    }

    public Multiset<T> getElements() {
        return elements;
    }

    public <U> Probabilities<U> map(Function<T, U> mapper) {
        final Probabilities<U> result = new Probabilities<>();
        elements.forEach(element -> result.add(mapper.apply(element)));
        return result;
    }

    public EnumeratedDistribution<T> toEnumeratedDistribution() {
        final List<Pair<T, Double>> pmf = new ArrayList<>();
        elements.forEachEntry((element, count) -> pmf.add(new Pair<>(element, ((double) (count)))));

        return new EnumeratedDistribution<>(pmf);
    }

}
