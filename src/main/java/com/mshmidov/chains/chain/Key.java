package com.mshmidov.chains.chain;

import java.util.Collection;

public interface Key<T> {

    int order();

    Collection<T> values();

    Key<T> append(T element);

}
