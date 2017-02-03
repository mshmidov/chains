package com.mshmidov.chains.markov;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

final class BoundedChainIterator<T> implements Iterator<T> {

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    private final MarkovChain<T> markovChain;

    private final long maximumElements;

    private boolean terminated = false;

    private long returnedElements = 0L;

    private T nextElement;

    private Key<T> nextKey;

    public BoundedChainIterator(MarkovChain<T> markovChain, long maximumElements) {
        checkArgument(maximumElements > 0, "Maximum number of expected returnedElements should be positive");

        this.markovChain = requireNonNull(markovChain);
        this.maximumElements = maximumElements;

        nextKey = markovChain.getStartingKeys().choose(random.nextDouble());

        cacheNext();
    }

    @Override
    public boolean hasNext() {
        return !terminated && (returnedElements < maximumElements);
    }

    @Override
    public T next() {

        if (terminated || returnedElements >= maximumElements) {
            throw new NoSuchElementException();
        }

        final T elementToReturn = this.nextElement;

        returnedElements++;
        cacheNext();

        return elementToReturn;
    }

    private void cacheNext() {

        if (returnedElements < markovChain.order()) {
            // returning elements from starting key
            nextElement = nextKey.values()[(Math.toIntExact(returnedElements))];

        } else {
            nextElement = markovChain.getNextElements(nextKey).map(choice -> choice.choose(random.nextDouble())).orElse(markovChain.terminalSymbol());
            nextKey = nextKey.append(nextElement);
        }

        if (nextElement.equals(markovChain.terminalSymbol())) {
            terminated = true;
        }
    }
}
