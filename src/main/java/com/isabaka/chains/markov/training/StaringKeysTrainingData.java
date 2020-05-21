package com.isabaka.chains.markov.training;

import java.util.Collection;

import com.isabaka.chains.markov.Key;
import com.isabaka.chains.markov.data.Probabilities;

public interface StaringKeysTrainingData<T> {

    void acceptElement(T element);

    void acceptCorpus(Collection<T> elements);

    Probabilities<Key<T>> getStartingKeys();

}
