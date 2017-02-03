package com.mshmidov.chains.markov;

import java.util.List;

public interface Key<T> {

    int order();

    List<T> values();

    Key<T> append(T element);

}
