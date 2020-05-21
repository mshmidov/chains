package com.isabaka.chains.markov.training;

import java.util.Collection;
import java.util.stream.Collectors;

import com.isabaka.chains.markov.Key;

class FirstKeysOfCorpus<T> implements StaringKeysTrainingData<T> {

    private final int order;

    private final Probabilities<Key<T>> startingKeys = new Probabilities<>();

    public FirstKeysOfCorpus(int order) {
        this.order = order;
    }

    @Override
    public void acceptElement(T element) {
        // do nothing
    }

    @Override
    public void acceptCorpus(Collection<T> elements) {
        if (elements.size() >= order) {
            startingKeys.add(new Key<>(elements.stream().limit(order).collect(Collectors.toList())));
        }
    }

    @Override
    public Probabilities<Key<T>> getStartingKeys() {
        return startingKeys;
    }

}
