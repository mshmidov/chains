package com.isabaka.chains.markov;

import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.isabaka.chains.markov.training.Training;
import org.apache.commons.math3.distribution.EnumeratedDistribution;

public final class MarkovChain<T> {

    private final int order;
    private final Map<Key<T>, EnumeratedDistribution<T>> chain;
    private final EnumeratedDistribution<Key<T>> startingKeys;

    public MarkovChain(Training<T> training) {

        this.order = training.getOrder();

        this.chain = training.getTrainingData()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toEnumeratedDistribution()));

        this.startingKeys = training.getStaringKeys().toEnumeratedDistribution();
    }

    public int getOrder() {
        return order;
    }

    public Key<T> randomStartingKey() {
        return startingKeys.sample();
    }

    public Optional<T> nextToken(Key<T> nextKey) {
        return Optional.ofNullable(chain.get(nextKey)).map(EnumeratedDistribution::sample);
    }

    public Stream<T> stream(Key<T> firstKey, T terminalElement) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(new InfiniteChainIterator<>(this, firstKey, terminalElement), Spliterator.IMMUTABLE),
                false);
    }

}
