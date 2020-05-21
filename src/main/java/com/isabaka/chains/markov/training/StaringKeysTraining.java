package com.isabaka.chains.markov.training;

import java.util.Collection;

import com.google.common.collect.Multiset;
import com.isabaka.chains.markov.Key;

public interface StaringKeysTraining<T> {

    void acceptElement(T element);

    void acceptCorpus(Collection<T> elements);

    Multiset<Key<T>> getStartingKeys();

}
