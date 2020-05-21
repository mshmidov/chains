package com.isabaka.chains.markov.data;

import java.util.Objects;
import java.util.StringJoiner;

public class KeyIndex {

    private final int index;

    public KeyIndex(int index) {
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
        KeyIndex keyIndex = (KeyIndex) o;
        return index == keyIndex.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", KeyIndex.class.getSimpleName() + "[", "]")
                .add("index=" + index)
                .toString();
    }

}
