package com.mshmidov.chains.random;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Multiset;

import static java.util.Objects.requireNonNull;

public final class WeightedRandom<T> {

    private final ImmutableSortedMap<Double, T> values;

    private final double total;

    private WeightedRandom(Builder<T> builder) {
        this.total = builder.total;
        this.values = builder.values.build();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> WeightedRandom<T> fromMultiset(Multiset<T> multiset) {
        final Builder<T> builder = builder();

        multiset.elementSet().forEach(element -> builder.add(multiset.count(element), element));

        return builder.build();
    }

    public T choose(double random) {
        return requireNonNull(values.ceilingEntry(random * total).getValue());
    }

    public ImmutableSortedMap<Double, T> values() {
        return values;
    }

    public double total() {
        return total;
    }

    public static final class Builder<T> {

        private final ImmutableSortedMap.Builder<Double, T> values = ImmutableSortedMap.naturalOrder();
        private double total = 0;

        private Builder() {
        }

        public Builder<T> add(double weight, T value) {

            if (weight > 0) {
                total += weight;
                values.put(total, value);
            }

            return this;
        }

        public WeightedRandom<T> build() {
            return new WeightedRandom<>(this);
        }
    }

}
