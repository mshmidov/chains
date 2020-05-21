package com.isabaka.chains.markov;

import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.isabaka.chains.markov.data.FinishedTraining;
import com.isabaka.chains.markov.data.TrainingData;
import com.isabaka.chains.markov.data.TrainingUnpacker;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

public final class MarkovChain<T> {

    private final int order;
    private final Map<Key<T>, EnumeratedDistribution<T>> chain;
    private final EnumeratedDistribution<Key<T>> startingKeys;
    private final T terminalElement;

    public MarkovChain(FinishedTraining<T> finishedTraining, T terminalElement) {
        final TrainingUnpacker<T> unpacker = new TrainingUnpacker<>(finishedTraining);

        this.order = unpacker.getOrder();
        this.chain = unpacker.getChain();
        this.startingKeys = unpacker.getStartingKeys();
        this.terminalElement = terminalElement;
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
