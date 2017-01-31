package com.mshmidov.chains.sequence;

import com.google.common.collect.Multiset;
import com.mshmidov.chains.chain.Key;
import com.mshmidov.chains.chain.MarkovChain;
import com.mshmidov.chains.random.WeightedRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public final class SequenceGenerator<T> {

    private final MarkovChain<T> chain;

    public SequenceGenerator(MarkovChain<T> chain) {
        this.chain = chain;
    }

    public Collection<T> newSequence(int maxLength) {
        final List<T> result = new ArrayList<>();

        final ThreadLocalRandom random = ThreadLocalRandom.current();

        Key<T> key = chain.getStartingKeys().choose(random.nextDouble());

        result.addAll(key.values());

        Optional<WeightedRandom<T>> nextElements = chain.getNextElements(key);

        while (nextElements.isPresent() && result.size() <= maxLength) {
            final T element = nextElements.get().choose(random.nextDouble());
            result.add(element);

            key = key.append(element);
            nextElements = chain.getNextElements(key);
        }

        return result;
    }
}
