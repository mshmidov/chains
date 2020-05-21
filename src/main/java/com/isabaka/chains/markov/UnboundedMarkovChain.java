package com.isabaka.chains.markov;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import static com.google.common.base.Preconditions.checkArgument;

public final class UnboundedMarkovChain<T> {

    private final int order;

    public static <T> Collector<T, Accumulator<T>, UnboundedMarkovChain<T>> collector(int order, T terminalElement, Predicate<Key<T>> startingKeyCriteria) {
        return Collector.of(
                () -> new Accumulator<>(order, startingKeyCriteria),
                Accumulator::accumulate,
                Accumulator::combine,
                accumulator -> new UnboundedMarkovChain<T>(accumulator, terminalElement));
    }

    public static <T> Collector<Iterable<T>, AccumulatorOfIterables<T>, UnboundedMarkovChain<T>> learnFromMultipleCorpus(int order, T terminalElement) {
        return Collector.of(
                () -> new AccumulatorOfIterables<>(order),
                AccumulatorOfIterables::accumulateIterable,
                AccumulatorOfIterables::combine,
                accumulator -> new UnboundedMarkovChain<T>(accumulator, terminalElement));
    }

    private final T terminalElement;
    private final Map<Key<T>, EnumeratedDistribution<T>> chain;
    private final EnumeratedDistribution<Key<T>> startingKeys;

    UnboundedMarkovChain(AbstractAccumulator<T> accumulator, T terminalElement) {
        this.order = accumulator.order;
        this.chain = new HashMap<>(accumulator.chain.size());
        this.terminalElement = terminalElement;

        accumulator.chain.forEach((key, data) -> {
            final double size = data.size();
            chain.put(key,
                    new EnumeratedDistribution<>(data.entrySet().stream()
                            .map(entry -> new Pair<>(entry.getElement(), entry.getCount() / size))
                            .collect(Collectors.toList())));
        });

        this.startingKeys = new EnumeratedDistribution<>(accumulator.getStartingKeys().stream()
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
                Spliterators.spliteratorUnknownSize(new UnboundedChainIterator<>(this, firstKey), Spliterator.IMMUTABLE),
                false);
    }

}
