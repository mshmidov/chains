package com.isabaka.chains.markov.training;

import java.util.Collection;

import com.isabaka.chains.markov.Key;

public interface StaringKeysTrainingData<T> {

    void acceptElement(T element);

    void acceptCorpus(Collection<T> elements);

    Probabilities<Key<T>> getStartingKeys();

}
