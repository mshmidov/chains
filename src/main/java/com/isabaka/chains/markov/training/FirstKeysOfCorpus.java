package com.isabaka.chains.markov.training;

import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.isabaka.chains.markov.ArrayKey;
import com.isabaka.chains.markov.Key;

class FirstKeysOfCorpus<T> implements StaringKeysTraining<T> {

    private final int order;

    private final Multiset<Key<T>> startingKeys = HashMultiset.create();

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
            startingKeys.add(new ArrayKey<T>((T[]) elements.stream().limit(order).collect(Collectors.toList()).toArray()));
        }
    }

    @Override
    public Multiset<Key<T>> getStartingKeys() {
        return startingKeys;
    }

}
