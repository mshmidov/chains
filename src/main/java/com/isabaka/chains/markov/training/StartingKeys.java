package com.isabaka.chains.markov.training;

import java.util.function.Predicate;

import com.isabaka.chains.markov.Key;

public final class StartingKeys {

    private StartingKeys() {
    }

    public static <T> StartingKeysExtraction<T> firstKeyOfCorpus() {
        return FirstKeysOfCorpus::new;
    }

    public static <T> StartingKeysExtraction<T> anyKeySatisfying(Predicate<Key<T>> condition) {
        return order -> new AnyKeysSatisfyingCondition<>(order, condition);
    }
}
