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

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.isabaka.chains.util.DisplacementBuffer;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import static com.google.common.base.Preconditions.checkArgument;

public final class UnboundedMarkovChain<T> {

    private final int order;

    public static <T> Collector<T, Accumulator<T>, UnboundedMarkovChain<T>> collector(int order, T terminalElement, Predicate<Key<T>> startingKeyCriteria) {
        return Collector.of(
                () -> new Accumulator<>(order),
                Accumulator::accumulate,
                Accumulator::combine,
                accumulator -> new UnboundedMarkovChain<T>(accumulator, terminalElement, startingKeyCriteria));
    }

    private final T terminalElement;
    private final Map<Key<T>, EnumeratedDistribution<T>> chain;
    private final EnumeratedDistribution<Key<T>> startingKeys;

    UnboundedMarkovChain(Accumulator<T> accumulator, T terminalElement, Predicate<Key<T>> startingKeyCriteria) {
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

        this.startingKeys = new EnumeratedDistribution<>(chain.keySet().stream()
                .filter(startingKeyCriteria)
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

    public static class Accumulator<T> {

        private final int order;

        private final DisplacementBuffer<T> buffer;

        private final Map<Key<T>, Multiset<T>> chain = new HashMap<>();

        private Accumulator(int order) {
            this.order = order;
            this.buffer = new DisplacementBuffer<>(order);
        }

        public void accumulate(T token) {
            if (buffer.isFull()) {
                final Key<T> key = new ArrayKey<>(buffer.getData());
                put(key, token);
            }

            buffer.add(token);
        }

        public Accumulator<T> combine(Accumulator<T> other) {
            if (other.order != this.order) {
                throw new IllegalArgumentException("Unable to combine Accumulators of different order");
            }

            other.chain.forEach((key, tokens) -> tokens.forEachEntry((token, count) -> put(key, token, count)));
            return this;
        }

        private void put(Key<T> key, T value) {
            checkArgument(key.order() == order);

            chain.computeIfAbsent(key, any -> HashMultiset.create()).add(value);
        }

        private void put(Key<T> key, T value, int count) {
            checkArgument(key.order() == order);

            chain.computeIfAbsent(key, any -> HashMultiset.create()).add(value, count);
        }

    }

}
