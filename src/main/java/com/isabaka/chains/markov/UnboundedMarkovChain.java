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

    public abstract static class AbstractAccumulator<T> {

        protected final int order;
        protected final DisplacementBuffer<T> buffer;
        protected final Map<Key<T>, Multiset<T>> chain = new HashMap<>();

        protected AbstractAccumulator(int order) {
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

        public abstract Multiset<Key<T>> getStartingKeys();

        protected void combine(AbstractAccumulator<T> other) {
            if (other.order != this.order) {
                throw new IllegalArgumentException("Unable to combine Accumulators of different order");
            }

            other.chain.forEach((key, tokens) -> tokens.forEachEntry((token, count) -> put(key, token, count)));
        }

        protected void put(Key<T> key, T value) {
            checkArgument(key.order() == order);

            chain.computeIfAbsent(key, any -> HashMultiset.create()).add(value);

        }

        protected void put(Key<T> key, T value, int count) {
            checkArgument(key.order() == order);

            chain.computeIfAbsent(key, any -> HashMultiset.create()).add(value, count);
        }

    }

    public static class Accumulator<T> extends AbstractAccumulator<T> {

        private final Predicate<Key<T>> startingKeyCriteria;
        private final Multiset<Key<T>> startingKeys = HashMultiset.create();

        private Accumulator(int order, Predicate<Key<T>> startingKeyCriteria) {
            super(order);
            this.startingKeyCriteria = startingKeyCriteria;
        }

        public Multiset<Key<T>> getStartingKeys() {
            return startingKeys;
        }

        public Accumulator<T> combine(Accumulator<T> other) {
            super.combine(other);
            other.startingKeys.forEachEntry((key, count) -> this.startingKeys.add(key, count));
            return this;
        }

        protected void put(Key<T> key, T value) {
            super.put(key, value);
            if (startingKeyCriteria.test(key)) {
                startingKeys.add(key);
            }
        }

    }

    public static class AccumulatorOfIterables<T> extends AbstractAccumulator<T> {

        private final Multiset<Key<T>> startingKeys = HashMultiset.create();

        private AccumulatorOfIterables(int order) {
            super(order);
        }

        public Multiset<Key<T>> getStartingKeys() {
            return startingKeys;
        }

        public void accumulateIterable(Iterable<T> tokens) {
            final DisplacementBuffer<T> keyBuffer = new DisplacementBuffer<>(order);
            boolean noStartingKey = true;

            for (T token : tokens) {
                accumulate(token);

                if (noStartingKey) {
                    keyBuffer.add(token);
                }

                if (keyBuffer.isFull()) {
                    startingKeys.add(new ArrayKey<>(keyBuffer.getData()));
                    keyBuffer.clear();
                    noStartingKey = false;
                }

            }
        }

        public AccumulatorOfIterables<T> combine(AccumulatorOfIterables<T> other) {
            super.combine(other);
            other.startingKeys.forEachEntry((key, count) -> this.startingKeys.add(key, count));
            return this;
        }

    }

}
