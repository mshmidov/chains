package com.isabaka.chains.markov;

import java.util.Collection;

import com.google.common.collect.Multiset;

public interface StartingKeysStrategy<T> {

    void acceptElement(T element);

    void acceptCorpus(Collection<T> elements);

    Multiset<Key<T>> getStartingKeys();

}
