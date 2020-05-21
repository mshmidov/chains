package com.isabaka.chains.markov;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import static com.google.common.base.Preconditions.checkArgument;

public final class MarkovChain<T> {

    private final int order;

    private final T terminalElement;
    private final Map<Key<T>, EnumeratedDistribution<T>> chain;
    private final EnumeratedDistribution<Key<T>> startingKeys;

    public MarkovChain(Training<T> training, T terminalElement) {
        this.order = training.getOrder();
        this.chain = new HashMap<>(training.getChain().size());
        this.terminalElement = terminalElement;

        training.getChain().forEach((key, data) -> {
            final double size = data.size();
            chain.put(key,
                    new EnumeratedDistribution<>(data.entrySet().stream()
                            .map(entry -> new Pair<>(entry.getElement(), entry.getCount() / size))
                            .collect(Collectors.toList())));
        });

        this.startingKeys = new EnumeratedDistribution<>(training.getStartingKeys().stream()
                .map(key -> new Pair<>(key, 1d))
                .collect(Collectors.toList()));
    }

    public int getOrder() {
        return order;
    }

    public T getTerminalElement() {
        return terminalElement;
    }

    public Key<T> randomStartingKey() {
        return startingKeys.sample();
    }

    public Optional<T> nextToken(Key<T> nextKey) {
        return Optional.ofNullable(chain.get(nextKey)).map(EnumeratedDistribution::sample);
    }

    public Stream<T> stream(Key<T> firstKey) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(new InfiniteChainIterator<>(this, firstKey), Spliterator.IMMUTABLE),
                false);
    }

}
