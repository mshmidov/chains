package com.isabaka.chains.markov;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;

import static java.util.Objects.requireNonNull;

final class InfiniteChainIterator<T> implements Iterator<T> {

    private final MarkovChain<T> markovChain;

    private Deque<T> buffer = new ArrayDeque<>();

    public InfiniteChainIterator(MarkovChain<T> markovChain, Key<T> firstKey) {
        this.markovChain = requireNonNull(markovChain);

        Collections.addAll(buffer, firstKey.values());
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        if (buffer.size() == markovChain.getOrder()) {
            final ArrayKey<T> key = new ArrayKey(buffer.toArray());
            markovChain.nextToken(key).ifPresentOrElse(
                    buffer::add,
                    () -> {
                        buffer.add((markovChain.getTerminalElement()));
                        Collections.addAll(buffer, markovChain.randomStartingKey().values());
                    });
        }

        return buffer.poll();
    }

}
