package com.mshmidov.chains.chain;

import java.util.ArrayDeque;

final class DisplacementBuffer<T> {

    private final ArrayDeque<T> data;

    private final int size;

    public DisplacementBuffer(int size) {
        this.size = size;
        data = new ArrayDeque<>(size);
    }

    public void add(T item) {
        data.addLast(item);
        if (data.size() > size) {
            data.removeFirst();
        }
    }

    public int getDataSize() {
        return data.size();
    }

    public T[] getData() {
        return ((T[]) data.toArray());
    }

    public void clear() {
        data.clear();
    }
}
