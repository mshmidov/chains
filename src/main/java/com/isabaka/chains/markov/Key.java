package com.isabaka.chains.markov;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

public final class Key<T> {

    private final List<T> values;

    public Key(T... elements) {
        this.values = ImmutableList.copyOf(elements);
    }

    public Key(Collection<T> values) {
        this.values = ImmutableList.copyOf(values);
    }

    public List<T> values() {
        return values;
    }

    public Key<T> append(T element) {
        return new Key<>(Stream.concat(values.stream().skip(1), Stream.of(element)).collect(Collectors.toList()));
    }

    public int order() {
        return values.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Key)) {
            return false;
        }
        final Key<?> other = (Key<?>) o;
        return Objects.equals(values, other.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public String toString() {
        return values.stream().map(String::valueOf).collect(Collectors.joining(""));
    }

}
