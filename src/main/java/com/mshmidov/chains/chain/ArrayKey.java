package com.mshmidov.chains.chain;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Collection;

final class ArrayKey<T> implements Key<T> {

    private static final Joiner JOINER = Joiner.on("");

    private final T[] values;

    private final int hashCode;

    ArrayKey(T[] values) {
        this.values = Arrays.copyOf(values, values.length);
        this.hashCode = Arrays.hashCode(values);
    }

    @Override
    public Collection<T> values() {
        return ImmutableList.copyOf(values);
    }

    @Override
    public Key<T> append(T element) {
        @SuppressWarnings("unchecked")
        final T[] newKey = Arrays.copyOf(values, values.length);
        System.arraycopy(newKey, 1, newKey, 0, newKey.length - 1);
        newKey[newKey.length - 1] = element;

        return new ArrayKey<>(newKey);
    }

    @Override
    public int order() {
        return values.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrayKey)) {
            return false;
        }
        final ArrayKey<?> other = (ArrayKey<?>) o;
        return Arrays.equals(values, other.values);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return JOINER.join(values);
    }
}
