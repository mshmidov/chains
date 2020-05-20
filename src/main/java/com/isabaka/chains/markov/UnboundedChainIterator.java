package com.isabaka.chains.markov;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import static java.util.Objects.requireNonNull;

final class UnboundedChainIterator<T> implements Iterator<T> {

    private final UnboundedMarkovChain<T> markovChain;

    private Deque<T> buffer = new ArrayDeque<>();

    public UnboundedChainIterator(UnboundedMarkovChain<T> markovChain, Key<T> firstKey) {
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
