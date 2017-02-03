package com.mshmidov.chains.markov;

public interface Key<T> {

    int order();

    T[] values();

    Key<T> append(T element);

}
