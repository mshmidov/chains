package com.isabaka.chains.markov.data;

import java.util.Objects;
import java.util.StringJoiner;

public class ElementIndex {

    private final int index;

    public ElementIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ElementIndex keyIndex = (ElementIndex) o;
        return index == keyIndex.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ElementIndex.class.getSimpleName() + "[", "]")
                .add("index=" + index)
                .toString();
    }

}
