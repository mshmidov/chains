package com.isabaka.chains.markov;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static java.util.Objects.requireNonNull;

final class InfiniteChainIterator<T> implements Iterator<T> {

    private final MarkovChain<T> markovChain;

    private final T terminalElement;

    private Deque<T> buffer = new ArrayDeque<>();

    public InfiniteChainIterator(MarkovChain<T> markovChain, Key<T> firstKey, T terminalElement) {
        this.markovChain = requireNonNull(markovChain);
        this.terminalElement = terminalElement;

        buffer.addAll(firstKey.values());
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        if (buffer.size() == markovChain.getOrder()) {
            final Key<T> key = new Key<T>(buffer);
            markovChain.nextToken(key).ifPresentOrElse(
                    buffer::add,
                    () -> {
                        buffer.add(terminalElement);
                        buffer.addAll(markovChain.randomStartingKey().values());
                    });
        }

        return buffer.poll();
    }

}
